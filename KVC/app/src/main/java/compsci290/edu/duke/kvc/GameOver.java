package compsci290.edu.duke.kvc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Bao on 4/22/2017.
 */

public class GameOver extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try {
            VictoryScreech.screech();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
