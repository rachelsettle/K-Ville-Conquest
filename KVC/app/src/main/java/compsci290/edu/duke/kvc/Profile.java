package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    private TextView mUserName;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUserName = (TextView) this.findViewById(R.id.userName);
        mUserName.setText(getIntent().getExtras().getString("Email"));
        mSharedPref = getSharedPreferences("GameInfo", Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
    }

    public void onGameStartClick(View button){
        Intent i = new Intent(Profile.this, CharacterSelectScreen.class);
        startActivity(i);
    }

    public void onSignOutClick(View Button){
        mEditor.putBoolean("LoggedInStatus", false);
        Intent i = new Intent(Profile.this, MainActivity.class);
        startActivity(i);
    }
}
