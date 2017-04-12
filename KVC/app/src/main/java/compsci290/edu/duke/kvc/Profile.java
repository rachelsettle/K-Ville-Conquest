package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    private TextView mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPref.initialize(Profile.this.getApplicationContext());
        mUserName = (TextView) this.findViewById(R.id.userName);
        mUserName.setText(SharedPref.read("Username", ""));
    }

    public void onGameStartClick(View button){
        Intent i = new Intent(Profile.this, CharacterSelectScreen.class);
        startActivity(i);
    }

    public void onSignOutClick(View Button){
        SharedPref.write("LoggedInStatus", false);
        Intent i = new Intent(Profile.this, MainActivity.class);
        startActivity(i);
    }
}
