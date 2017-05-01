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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    private TextView mUserName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPref.initialize(Profile.this.getApplicationContext());
        mUserName = (TextView) this.findViewById(R.id.userName);
        firebaseAuth = FirebaseAuth.getInstance();
        setUserName();
    }

    private void setUserName(){
        //use the DB reference to read in the first name of the user
        mDBRef= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                if (u != null) {
                    mUserName.setText(u.firstName + "!");
                }

                else{
                    mUserName.setText("Player!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("readUserFailed", "onCalledCalled", databaseError.toException());
            }
        };

        mDBRef.addValueEventListener(listener);
    }

    public void onGameStartClick(View button){
        Intent i = new Intent(Profile.this, InstructionScreen.class);
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

    public void onSettingsClick (View button){
        Intent i = new Intent(Profile.this, SettingsActivity.class);
        startActivity(i);
    }
}
