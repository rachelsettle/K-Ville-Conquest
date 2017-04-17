package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    private TextView mUserName;
    private DatabaseReference mDBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPref.initialize(Profile.this.getApplicationContext());
        mUserName = (TextView) this.findViewById(R.id.userName);
        mUserName.setText(SharedPref.read("Username", ""));

        //delete everything here below in the method when done testing
        mDBRef= FirebaseDatabase.getInstance().getReference("");
    }

    public void onGameStartClick(View button){
        Intent i = new Intent(Profile.this, CharacterSelectScreen.class);
        startActivity(i);
    }

    public void onSignOutClick(View button){
        SharedPref.write("LoggedInStatus", false);
        Intent i = new Intent(Profile.this, MainActivity.class);
        startActivity(i);
    }

    public void onHighScoreClick(View button){
        Intent i = new Intent(Profile.this, LocalHighScores.class);
        startActivity(i);
    }
}
