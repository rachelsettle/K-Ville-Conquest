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

import java.lang.reflect.Array;

/**
 * Created by Bao on 4/19/2017.
 */

public class GlobalHighScoreAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private String[] mUserNames;
    private String[] mCharacterNames;
    private int[] mScores;
    private int mNullIndex;

    public GlobalHighScoreAdapter(Activity context, String[] names, String[] chars, int[] scores,
                                  int nullIndex) {
        super(context, R.layout.global_score_item, names);
        mContext = context;
        mUserNames = names;
        mCharacterNames = chars;
        mScores = scores;
        mNullIndex = nullIndex;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.global_score_item, null, true);

        //if the case where we don't have at least 20 scores
        //this will return a blank row for indexes greater than or
        //equal to the first time that the fields start becoming null
        if (position >= mNullIndex && mNullIndex > -1){
            rowView.setVisibility(View.INVISIBLE);
            return rowView;
        }

        TextView playerName = (TextView) rowView.findViewById(R.id.playerName);
        TextView playerScore = (TextView) rowView.findViewById(R.id.globalScoreText);
        TextView playerCharacter = (TextView) rowView.findViewById(R.id.globalCharacerText);

        playerName.setText("Name: " + mUserNames[position]);
        playerScore.setText("Score: " + mScores[position]);
        playerCharacter.setText("Character: " + mCharacterNames[position]);

        return rowView;
    }
}
