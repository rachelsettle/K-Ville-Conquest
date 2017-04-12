package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //go to profile page if already loggined in
    public boolean mLoggedIn;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSharedPref = getSharedPreferences("GameInfo", Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
        mLoggedIn = mSharedPref.getBoolean("LoggedInStatus", false);

        //go to registration and login screen if you're no already logged in
        if (!mLoggedIn) {
            setContentView(R.layout.activity_main);
        }

        //go to profile screen if you have not logged out yet
        else{
            mEditor.putBoolean("LoggedInStatus", true);
            Intent profileIntent = new Intent(MainActivity.this, Profile.class);
            startActivity(profileIntent);
        }
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
