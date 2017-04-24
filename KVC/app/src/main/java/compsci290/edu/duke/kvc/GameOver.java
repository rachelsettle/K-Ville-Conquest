package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by Bao on 4/22/2017.
 */

public class GameOver extends Activity{
    int mScore;
    TextView scoreDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle score = getIntent().getExtras();
        mScore = score.getInt("Score");
        scoreDisplay.setText(mScore);

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

}
