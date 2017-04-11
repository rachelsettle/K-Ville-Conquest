package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
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

        ImageView rowImage = (ImageView) rowView.findViewById(R.id.charIcon);
        TextView rowButton = (TextView) rowView.findViewById(R.id.charName);

        rowImage.setImageResource(mIDs[position]);

        //chop ending off
        String fullName = mNames[position];
        int endingIndex = fullName.indexOf("_character");
        fullName = fullName.substring(0, endingIndex);
        rowButton.setText(fullName);

        return rowView;
    }
}
