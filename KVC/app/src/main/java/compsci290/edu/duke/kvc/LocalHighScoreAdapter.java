package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Bao on 4/14/2017.
 */

public class LocalHighScoreAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private String[] mUsersCol;
    private String[] mCharctersCol;
    private String[] mScoresCol;
    private FirebaseAuth firebaseAuth;


    public LocalHighScoreAdapter(Activity context, String[] userIDs, String[] charactersUsed,
                                 String[] scores) {
        super(context, R.layout.local_score_item,userIDs);
        mContext = context;
        mUsersCol = userIDs;
        mCharctersCol = charactersUsed;
        mScoresCol = scores;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.local_score_item, null, true);

        //fill in each row with the userID, character used, and score
        TextView characterNameText = (TextView) rowView.findViewById(R.id.characterNameText);
        TextView scoreText = (TextView) rowView.findViewById(R.id.scoreText);

        characterNameText.setText("Character: " + mCharctersCol[position]);
        scoreText.setText("Score: " + mScoresCol[position]);

        return rowView;
    }
}
