package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

/**
 * Created by Bao on 4/9/2017.
 */

public class GameScreen extends Activity {

    private int mCharacterID;
    private String mCharacterName;
    private ImageView mCharacterImage;
    private FirebaseAuth firebaseAuth;
    private LocalScoreDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        mDBHelper = new LocalScoreDBHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mCharacterImage = (ImageView) this.findViewById(R.id.charImage);

        SharedPref.initialize(GameScreen.this.getApplicationContext());

        //default character is the first character
        mCharacterID = SharedPref.read("charID", CharacterSelectScreen.sCharacterIDs[0]);
        mCharacterImage.setImageResource(mCharacterID);
        mCharacterName = SharedPref.read("charName", CharacterSelectScreen.sCharacterNames[0]);
        int randomScore = giveScore();
        //write user_ID, character, and score to database
        writeScore(randomScore);
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
    }
}
