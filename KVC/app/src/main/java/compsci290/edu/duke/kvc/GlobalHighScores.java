package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Bao on 4/14/2017.
 */

public class GlobalHighScores extends AppCompatActivity {

    private ListView mScoreList;
    private TextView mGlobalScoreInfoText;

    //used in adapter later
    //sPlayerNames[i] + sScores[i] + sCharacters[i] is one row
    public static String[] sPlayerNames;
    public static int[] sScores;
    public static String[] sCharacters;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_high_scores);
        mScoreList = (ListView) findViewById(R.id.globalScoresList);
        mGlobalScoreInfoText = (TextView) findViewById(R.id.globalScoreInfoText);
        get20HighestScores();
    }

    public void onHomeClick(View button){
        Intent i = new Intent(GlobalHighScores.this, Profile.class);
        startActivity(i);
    }

    //retrieves the 20 highest scores from Firebase
    //or however many there are if there are fewer than 20 recorded entries
    public void get20HighestScores(){
        final ArrayList<DataSnapshot> topScoresReferences = new ArrayList<DataSnapshot>();
        //used for the adapater laster
        final String[] userNames = new String[20];
        final String[] userCharacters = new String[20];
        final int[] userScores = new int[20];

        final DatabaseReference scores = FirebaseDatabase.getInstance().getReference().child("Scores");
        scores.limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    topScoresReferences.add(child);
                }

                //no scores at all
                if (topScoresReferences.size() == 0) {
                    mGlobalScoreInfoText.setText("No scores in the world yet!");
                    return;
                }

                //so that top score goes to the front
                Collections.reverse(topScoresReferences);
                int scoresFound = 0;
                int i = 0;

                //we either find the top 20 scores or (scoresFound)
                //all the scores if there's less than 20 (topScoresReferences.size())
                while (i < topScoresReferences.size() && scoresFound < 20) {
                    DataSnapshot score = topScoresReferences.get(i);
                    for (DataSnapshot userID : score.getChildren()) {
                        String firstName = "";
                        String lastName = "";
                        //the length of the arraylist below is the number of times this
                        //user acheived this particular score since they did it once
                        //for each character
                        ArrayList<String> charactersForThisUser = new ArrayList<String>();
                        for (DataSnapshot userField : userID.getChildren()) {
                            if (userField.getKey().equals("firstName")) {
                                firstName = userField.getValue(String.class);
                            } else if (userField.getKey().equals("lastName")) {
                                lastName = userField.getValue(String.class);
                            }
                            //list of characters
                            //pretty sure it hits this first since the children are
                            //stored in alphabetical order
                            //stuff all keys of children in the arraylist
                            else {
                                for (DataSnapshot characterName : userField.getChildren()) {
                                    charactersForThisUser.add(characterName.getKey());
                                }
                            }
                        }
                        int numNewScores = charactersForThisUser.size();
                        int charIndex = 0;
                        for (int scoreNum = scoresFound; (charIndex < numNewScores) &&
                                (scoreNum < 20); scoreNum++) {
                            userNames[scoreNum] = firstName + " " + lastName;
                            userCharacters[scoreNum] = charactersForThisUser.get(charIndex);
                            userScores[scoreNum] = Integer.parseInt(score.getKey());
                            charIndex++;
                        }
                        scoresFound = scoresFound + charactersForThisUser.size();
                    }
                    i++;
                }


                //there are fewer than 20 scores worldwide
                if (i != 20 && scoresFound != 20) {
                    mGlobalScoreInfoText.setText("Top " + scoresFound + " Scores in the World");
                } else {
                    mGlobalScoreInfoText.setText("Top 20 Scores in the Wolrd");
                }

                //don't send the adapter null values
                int nullIndex = -1;
                for (int j = 0; j < userNames.length; j++) {
                    if (userNames[j] == null) {
                        nullIndex = j;
                        break;
                    }
                }

                GlobalHighScoreAdapter adapter = new GlobalHighScoreAdapter(GlobalHighScores.this,
                        userNames, userCharacters, userScores, nullIndex);
                mScoreList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("noGlobalScores", "failed", databaseError.toException());
            }
        });
    }



}
