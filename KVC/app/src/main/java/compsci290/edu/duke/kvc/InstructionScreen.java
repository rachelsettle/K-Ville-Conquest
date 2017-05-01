package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Settle on 4/30/17.
 */



public class InstructionScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
    }

    public void onLetsPlayClick(View button){
        Intent i = new Intent(InstructionScreen.this, VictoryScreech.class);
        startActivity(i);
    }
}
