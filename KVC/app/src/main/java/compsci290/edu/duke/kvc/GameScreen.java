package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * Created by Bao on 4/9/2017.
 */

public class GameScreen extends AppCompatActivity implements View.OnTouchListener {

    private String mCharacterName;
    private FirebaseAuth firebaseAuth;
    private LocalScoreDBHelper mDBHelper;
    private DatabaseReference firebaseDBRoot;

    private ImageView tent;
    private ViewGroup mRootLayout;
    private float _xDelta;
    int width;
    private SoundHelper mSoundHelper;
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
        tent.setImageResource(SharedPref.read("charID", CharacterSelectScreen.sCharacterIDs[0]));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        tent.setLayoutParams(layoutParams);
        tent.setOnTouchListener(this);
        getWindow().setBackgroundDrawableResource(R.drawable.kvillebackground);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        tent.getLayoutParams().height = height - 50;
        mSoundHelper = new SoundHelper();
        mSoundHelper.prepMusicPlayer(this);
        mSoundHelper.playMusic();
       //mObstacle = new Obstacle(this,0xFFFF0000,100);
       //mObstacle.releaseObstacle(height,10);
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
