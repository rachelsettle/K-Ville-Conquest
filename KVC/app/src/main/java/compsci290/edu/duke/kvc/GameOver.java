package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Bao on 4/22/2017.
 */

public class GameOver extends Activity{
    int mScore;
    TextView scoreDisplay;
    private String mCharacterName;
    private FirebaseAuth firebaseAuth;
    private LocalScoreDBHelper mDBHelper;
    private DatabaseReference firebaseDBRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_activity);
        mDBHelper = new LocalScoreDBHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDBRoot = FirebaseDatabase.getInstance().getReference();
        SharedPref.initialize(GameOver.this.getApplicationContext());
        mCharacterName = SharedPref.read("charName", CharacterSelectScreen.sCharacterNames[0]);
        Bundle score = getIntent().getExtras();
        mScore = score.getInt("Score");
        scoreDisplay = (TextView) this.findViewById(R.id.gameOverScore);
        scoreDisplay.setText(mScore + "");
        writeScore(mScore);

        try {
            VictoryScreech.screech();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAgain(View view){
        Intent i = new Intent(GameOver.this,GameScreen.class);
        startActivity(i);
    }

    public void goToHome(View view){
        Intent j = new Intent(GameOver.this,Profile.class);
        startActivity(j);
    }

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


}
