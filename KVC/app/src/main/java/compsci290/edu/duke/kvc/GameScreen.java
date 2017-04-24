package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import compsci290.edu.duke.kvc.util.SoundHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * Created by Bao on 4/9/2017.
 */

public class GameScreen extends AppCompatActivity implements Obstacle.ObstacleListener, View.OnTouchListener {

    private String mCharacterName;
    private FirebaseAuth firebaseAuth;
    private LocalScoreDBHelper mDBHelper;
    private DatabaseReference firebaseDBRoot;


    private ImageView tent;
    private ViewGroup mRootLayout;
    private float _xDelta;
    int width;

    private SoundHelper mSoundHelper;
    private int mScreenWidth, mScreenHeight;
    private int mScore;
    TextView mScoreDisplay;
    private int mTentNumber;
    TextView mTentNumberDisplay;
    //private Obstacle mObstacle;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Bao's methods
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        mDBHelper = new LocalScoreDBHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();

        //points to the top of the JSON tree
        firebaseDBRoot = FirebaseDatabase.getInstance().getReference();


        SharedPref.initialize(GameScreen.this.getApplicationContext());

        //default character is the first character
        mCharacterName = SharedPref.read("charName", CharacterSelectScreen.sCharacterNames[0]);
        int randomScore = giveScore();
        //write user_ID, character, and score to database
        writeScore(randomScore);

        //Grant's methods
        mRootLayout = (ViewGroup) findViewById(R.id.root);
        tent = (ImageView) mRootLayout.findViewById(R.id.characterImage);
        if (SharedPref.read("position",1) != 0){
            tent.setImageResource(SharedPref.read("charID", CharacterSelectScreen.sCharacterIDs[0]));
        }
        else{
            //tent.setImageBitmap();
            Intent intent = getIntent();
            byte[] bytes = intent.getByteArrayExtra("BMP");
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            tent.setImageBitmap(getTriangleBitmap(bmp,170));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
        tent.setLayoutParams(layoutParams);
        tent.setOnTouchListener(this);

        //set background
        new backgroundSet().execute();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        tent.getLayoutParams().height = height;

        mSoundHelper = new SoundHelper();
        mSoundHelper.prepMusicPlayer(this);
        mSoundHelper.playMusic();

        mScoreDisplay = (TextView) findViewById(R.id.score);
        mTentNumberDisplay = (TextView) findViewById(R.id.tentNumber);

        //Gets Screen Height and Screen Width
        ViewTreeObserver viewTreeObserver = mRootLayout.getViewTreeObserver();
        if(viewTreeObserver.isAlive()){
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout(){
                    mRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mScreenHeight = mRootLayout.getHeight();
                    mScreenWidth = mRootLayout.getWidth();
                }
            });
        }
        startGameLoop();
    }


    // change background based on weather
    class backgroundSet extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... params) {
            double ans = 0;
            BufferedReader reader = null;
            StringBuffer json = new StringBuffer(1024);
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=4464368&units=imperial&appid=54aa8efad13a44916ed53266ee0ab168");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d("WEATHER", "connection is being made");
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String tmp = "";
                    try {
                        while ((tmp = reader.readLine()) != null)
                            json.append(tmp).append("\n");
                        reader.close();
                        try {
                            JSONObject data = new JSONObject(json.toString());
                            JSONObject m = data.getJSONObject("main");
                            ans = m.getDouble("temp");
                            Log.d("WEATHER","temperature is" + ans);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ans;
        }
        @Override
        protected void onPostExecute(Double t){
            if (t <= 32){
                getWindow().setBackgroundDrawableResource(R.drawable.weather_winter);
            }
            else if (t > 32 && t < 80){
                getWindow().setBackgroundDrawableResource(R.drawable.weather_normal);
            }
            else if (t >= 80){
                getWindow().setBackgroundDrawableResource(R.drawable.weather_spring);
            }
        }
    }
    public static Bitmap getTriangleBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());
        Point point1_draw = new Point(75, 0);
        Point point2_draw = new Point(0, 180);
        Point point3_draw = new Point(180, 180);
        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point1_draw.x, point1_draw.y);
        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);
        return output;
    }

    public void startGameLoop(){
        Log.d("TAG","GAME LOOP STARTED");
        mTentNumber=50;
        updateDisplay();
        ObstacleLauncher launcher = new ObstacleLauncher();
        launcher.execute(mTentNumber);
    }


    @Override
    public void didCollide(Obstacle obstacle, boolean userTouch) {
        mRootLayout.removeView(obstacle);
        if(userTouch){
            mScore=mScore+obstacle.getPoints();
        }
        updateDisplay();
    }

    private void updateDisplay() {
        mScoreDisplay.setText(String.valueOf(mScore));
        mTentNumberDisplay.setText(String.valueOf(mTentNumber));
    }

    private class ObstacleLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {


            int tentNumber = params[0];
            Log.d("TAG","OBSTACLE LAUNCHER RAN");

            while (tentNumber > 0) {

//              Get a random horizontal position for the next balloon
                Random random = new Random();
                int xPosition = random.nextInt((mScreenWidth)+1);
                publishProgress(xPosition);
                tentNumber--;

//              Wait a random number of milliseconds before looping
                int delay = 1500;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchObstacle(xPosition);
        }

    }

    private void launchObstacle(int x) {

        Obstacle obstacle = new Obstacle(GameScreen.this,"cup",100,tent);
        int mWidth = obstacle.getWidth();
        obstacle.setX((float) x+mWidth);
        obstacle.setY(0f-tent.getHeight());
        mRootLayout.addView(obstacle);
        obstacle.releaseObstacle(mScreenHeight, 2000);
    }

    //spit out a random score for sake of database testing
    private static int giveScore(){
        //returns score between [0, 1000]
        Random r = new Random();
        int randy = r.nextInt(1001);
        return randy;
    }

    private void writeScore(int score){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        //bind values we want to insert to their respective column names
        ContentValues values = new ContentValues();
        values.put(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_USER_ID, "" + firebaseAuth.getCurrentUser().getUid());
        values.put(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_CHARACTER, mCharacterName);
        values.put(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_SCORE, score);

        Log.d("rowInserted", "" + firebaseAuth.getCurrentUser().getUid() + "| " + mCharacterName + "| " + score);

        //insert new score into the ScoreRecord table
        //this table only exists for this device
        db.insert(LocalScoreContract.LocalScoreRecord.TABLE_NAME, null, values);
        db.close();

        //copy to Firebase database
        //put everyone who scored the same
        getNames(score);
        String userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference currentUser = firebaseDBRoot.child("Scores").child(score + "").child(userID + "");
        currentUser.child("characters").child(mCharacterName).setValue("True");
    }

    //sets the first and last name of the logged in user
    //in the scores tree
    public void getNames(int score){
        String currentID = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference currentUser = firebaseDBRoot.child("Users").child(currentID);
        final DatabaseReference userScore = firebaseDBRoot.child("Scores").child(score + "").child(currentID + "");
        //only gets called once
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                userScore.child("firstName").setValue(u.firstName);
                userScore.child("lastName").setValue(u.lastName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("GameScreenError", "onCalledCalled", databaseError.toException());
            }
        });
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _xDelta = view.getX() - event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float newX =  event.getRawX() +_xDelta;
                if((newX <= 0 || newX >= width-view.getWidth())){
                    break;
                }
                view.animate()
                        .x(event.getRawX() + _xDelta)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }

    protected void onDestroy(){
        mSoundHelper.pauseMusic();
        super.onDestroy();
    }
}
