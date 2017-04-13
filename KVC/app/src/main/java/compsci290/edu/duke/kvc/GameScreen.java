package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import java.util.Random;

/**
 * Created by Bao on 4/9/2017.
 */

public class GameScreen extends Activity {

    private int mCharacterID;
    private int mScore;
    private ImageView mCharacterImage;

    private int windowWidth;
    private int windowHeight;
    private LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        mCharacterImage = (ImageView) this.findViewById(R.id.charImage);
        mCharacterID = getIntent().getExtras().getInt("charID");
        mCharacterImage.setImageResource(mCharacterID);

        //windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        //windowHeight = get
    }

    //spit out a random score for sake of database testing
    private int giveScore(){
        //returns score between [0, 1000]
        Random r = new Random();
        int randy = r.nextInt(1001);
        return randy;
    }

}
