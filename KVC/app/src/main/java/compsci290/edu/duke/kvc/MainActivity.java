package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private boolean mLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRegistrationClick(View button){
        Intent regisIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(regisIntent);
    }

    public void onLoginClick(View button){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_after_login);
    }


    public void onGameStartClick(View button){
        Intent gameStartIntent = new Intent(MainActivity.this, CharacterSelectScreen.class);
        startActivity(gameStartIntent);
    }

    public void onHighScoreClick(View button){

    }
    */
}
