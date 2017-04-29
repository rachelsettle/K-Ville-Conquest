package compsci290.edu.duke.kvc;

import android.content.Intent;
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

import compsci290.edu.duke.kvc.util.SoundHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Bao on 4/9/2017.
 */

public class GameScreen extends AppCompatActivity implements Obstacle.ObstacleListener, View.OnTouchListener {


    private int duration;
    private ImageView tent;
    private ViewGroup mRootLayout;
    private float _xDelta;
    int width;
    int height;
    private SoundHelper mSoundHelper;
    private int mScreenWidth, mScreenHeight;
    private int mScore;
    TextView mScoreDisplay;
    private int mTentNumber=50;
    TextView mTentNumberDisplay;
    private ArrayList<String> mObstacles;
    private String mTentName;
    private int mScoreMultiplier = 1;
    private int mTentSize = 300;
    private int mTankDamageTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        findObstacles();

        SharedPref.initialize(GameScreen.this.getApplicationContext());

        //default character is the first character
        duration = SharedPref.read("diffSettings",3000);

        mRootLayout = (ViewGroup) findViewById(R.id.root);
        tent = (ImageView) mRootLayout.findViewById(R.id.characterImage);

        // Inputting the customizable tent
        if (SharedPref.read("position",1) != 0){
            tent.setImageResource(SharedPref.read("charID", CharacterSelectScreen.sCharacterIDs[0]));
        }
        else{
            Intent intent = getIntent();
            byte[] bytes = intent.getByteArrayExtra("BMP");
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            tent.setImageBitmap(getTriangleBitmap(bmp,170));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mTentSize, mTentSize);
        tent.setLayoutParams(layoutParams);
        tent.setOnTouchListener(this);

        new backgroundSet().execute();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        height = displaymetrics.heightPixels;
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
        setTentPowers();
        startGameLoop();
    }

    public void setTentPowers(){
        mTentName = SharedPref.read("charName", "Duke Tent");
        Log.d("tentName", mTentName);
        //double the point values of everything
        if (mTentName.contains("Video Game Tent")){
            mScoreMultiplier = 2;
            return;
        }
        //double time duration
        else if (mTentName.contains("Duke Tent")){
            mTentNumber = mTentNumber*2;
            return;
        }
        //double tent size
        else if (mTentName.contains("Ice Cream Tent")){
            Log.d("chocolate", "fudge");
            mTentSize = mTentSize*2;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mTentSize, mTentSize);
            tent.setLayoutParams(layoutParams);
            tent.getLayoutParams().height = height;
            return;
        }
        //no point deduction for first 5 negatives
        //but nmakes things drop faster
        else if (mTentName.contains("Ocean Tent")) {
            mTankDamageTimes = 5;
            duration = duration-500;
            return;
        }
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
                            Log.d("WEATHER","temperature is " + ans);
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
            } catch (IOException e) {
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
        updateDisplay();
        ObstacleLauncher launcher = new ObstacleLauncher();
        launcher.execute(mTentNumber);
    }


    @Override
    public void didCollide(Obstacle obstacle, boolean userTouch) {
        mRootLayout.removeView(obstacle);
        if(userTouch){
            //don't change score if you have more tank times
            if (mTankDamageTimes > 0) {
                if (obstacle.getPoints() < 0) {
                    mTankDamageTimes--;
                    return;
                }
            }

            mScore = mScore + (obstacle.getPoints() * mScoreMultiplier);
        }
        updateDisplay();
    }

    private void updateDisplay() {
        mScoreDisplay.setText(String.valueOf(mScore));
    }

    private void updateDisplay(int mTentNumber) {
        mTentNumberDisplay.setText(String.valueOf(mTentNumber));
    }

    private class ObstacleLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {


            int tentNumber = params[0];
            Log.d("TAG","OBSTACLE LAUNCHER RAN");

            while (tentNumber > 0) {

//              Get a random horizontal position for the next balloon
                int xPosition = ThreadLocalRandom.current().nextInt(300,width-300);
                publishProgress(xPosition, tentNumber);
                tentNumber--;
//              Wait a random number of milliseconds before looping
                int delay = 1200;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameOver();
            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            int mTentNumber = values[1];
            launchObstacle(xPosition, mTentNumber);

        }

    }

    private void launchObstacle(int x, int mTentNumber) {
        Obstacle obstacle = new Obstacle(GameScreen.this,getObstacle()
                ,100,tent);
        int mWidth = obstacle.getWidth();
        obstacle.setX((float) x+mWidth);
        obstacle.setY(0f-tent.getHeight());
        mRootLayout.addView(obstacle);
        obstacle.releaseObstacle(mScreenHeight, duration);
        updateDisplay(mTentNumber);
    }

    //Just wrote this
    private String getObstacle(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 7);
        return mObstacles.get(randomNum);
    }
    //Just wrote this
    private ArrayList<String> findObstacles(){
        Field[] ID_Fields = R.drawable.class.getFields();
        mObstacles = new ArrayList<String>();
        for (Field f: ID_Fields){
            try{
                String fileName = f.getName();

                if (fileName.contains("obstacle")){
                    mObstacles.add(fileName);
                }
            }
            catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        return mObstacles;
    }

    public void gameOver(){
        mSoundHelper.stopMusic();
        Intent score = new Intent(GameScreen.this,GameOver.class);
        score.putExtra("Score",mScore);
        startActivity(score);
    }

    //spit out a random score for sake of database testing

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
