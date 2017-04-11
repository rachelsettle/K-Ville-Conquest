package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onGameStartClick(View button){
        Intent gameStartIntent = new Intent(MainActivity.this, CharacterSelectScreen.class);
        startActivity(gameStartIntent);
    }

    public void onHighScoreClick(View button){

    }

}
