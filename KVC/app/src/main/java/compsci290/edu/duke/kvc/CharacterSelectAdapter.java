package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bao on 4/9/2017.
 */

public class CharacterSelectAdapter extends ArrayAdapter<String>{

    private Activity mContext;
    private String[] mNames;
    private int[] mIDs;

    public CharacterSelectAdapter(Activity context, String[] names, int[] IDs) {
        super(context, R.layout.character_select_for_listview, names);
        mContext = context;
        mNames = names;
        mIDs = IDs;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.character_select_for_listview, null, true);

        ImageView charIcon = (ImageView) rowView.findViewById(R.id.charIcon);
        TextView charName = (TextView) rowView.findViewById(R.id.charName);

        //charIcon.setImageResource(mIDs[position]);

        //Start deleting
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), mIDs[position]);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, metrics);
        float heightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, metrics);
        Bitmap bmScaled = Bitmap.createScaledBitmap(bm, (int) widthPixels -150, (int) heightPixels, false);
        charIcon.setImageBitmap(bmScaled);
        //End deleting

        //need to display tent power
        String characterActualName = mNames[position];

        Log.d("charName", characterActualName);
        Log.d("position", position + "");

        if (characterActualName.contains("Duke")){
            charName.setText(characterActualName + ": \n" + "Doubles play time.");
        }

        else if (characterActualName.contains("Ice Cream")) {
            charName.setText(characterActualName + ": \n" + "Doubles tent size.");
        }

        else if (characterActualName.contains("Ocean")) {
            charName.setText(characterActualName + ": \n" + "No deductions for first 5 hits. Faster fall rates.");
        }

        else if (characterActualName.contains("Video Game")) {
            charName.setText(characterActualName + ": \n" + "Doubles points and deductions");
        }

        else {
            charName.setText(characterActualName + ": \n" + "No special powers.");
        }

        return rowView;
    }
}
