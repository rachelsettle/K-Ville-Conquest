package compsci290.edu.duke.kvc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    private TextView mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUserName = (TextView) this.findViewById(R.id.userName);
        mUserName.setText(getIntent().getExtras().getString("Email"));
    }

    public void onGameStartClick(View button){
        Intent i = new Intent(Profile.this, CharacterSelectScreen.class);
        startActivity(i);
    }
}
