package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Bao on 4/14/2017.
 */

public class LocalHighScores extends AppCompatActivity {

    private ListView mScoreList;
    private TextView mLocalScoreInfoText;
    private SQLiteOpenHelper mDBHelper;
    private FirebaseAuth firebaseAuth;

    //sUserIDCols[i] + sCharacterCols[i] + sScoreCols[i] = one row in table
    //will use them later in the adapter
    public static String[] sUserIDCols;
    public static String[] sCharacterCols;
    public static String[] sScoreCols;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_high_scores);
        mDBHelper = new LocalScoreDBHelper(this.getApplicationContext());
        mScoreList = (ListView) findViewById(R.id.localScoresList);
        mLocalScoreInfoText = (TextView) findViewById(R.id.localScoreInfoText);
        firebaseAuth = FirebaseAuth.getInstance();
        get20HighestScores();
    }

    public void onGlobalHighScoresClick(View button){
        Intent i = new Intent(LocalHighScores.this, GlobalHighScores.class);
        startActivity(i);
    }
    private void get20HighestScores() {
        //read from the LocalScoreRecord table
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {
                LocalScoreContract.LocalScoreRecord.COLUMN_NAME_USER_ID,
                LocalScoreContract.LocalScoreRecord.COLUMN_NAME_CHARACTER,
                LocalScoreContract.LocalScoreRecord.COLUMN_NAME_SCORE
        };

        String selection = LocalScoreContract.LocalScoreRecord.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = {"" + firebaseAuth.getCurrentUser().getUid()};
        String orderBy = LocalScoreContract.LocalScoreRecord.COLUMN_NAME_SCORE + " DESC";

        //want to mimic the SQL command:
        //SELECT DISTINCT *
        //FROM LocalScoreRecord
        //WHERE user_id = "this user name"
        //ORDER BY score DESC
        //LIMIT 20;

        //use distinct so that we dont have repeated entries over and over
        //ex: this person is really good with this character and keeps getting a certain score

        Cursor cursor = db.query(
                LocalScoreContract.LocalScoreRecord.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy,
                "20"
        );


        int numRows = cursor.getCount();
        //check to see if the the table is empty
        //do nothing with the list view and just change the textview
        if (numRows == 0) {
            mLocalScoreInfoText.setText("No scores on this account yet!");
            db.close();
            return;
        }

        else {
            cursor.moveToFirst();
            sUserIDCols = new String[numRows];
            sCharacterCols = new String[numRows];
            sScoreCols = new String[numRows];

            //iterate over SQL result and stuff in static arrays for the adapter
            int i = 0;
            while (i < numRows) {
                sUserIDCols[i] = cursor.getString(cursor.getColumnIndexOrThrow(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_USER_ID));
                sCharacterCols[i] = cursor.getString(cursor.getColumnIndexOrThrow(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_CHARACTER));
                sScoreCols[i] = cursor.getString(cursor.getColumnIndexOrThrow(LocalScoreContract.LocalScoreRecord.COLUMN_NAME_SCORE));
                i++;
                cursor.moveToNext();
            }
            mLocalScoreInfoText.setText("Top " + i + " Highest Scores on this Account");
            LocalHighScoreAdapter adapter = new LocalHighScoreAdapter(this, sUserIDCols, sCharacterCols, sScoreCols);
            mScoreList.setAdapter(adapter);
            db.close();
        }
    }

}
