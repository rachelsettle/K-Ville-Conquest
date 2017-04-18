package compsci290.edu.duke.kvc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDBRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initalize references
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mEmailInput = (EditText) this.findViewById(R.id.emailInputRegistration);
        mPasswordInput = (EditText) this.findViewById(R.id.passwordInputRegistration);
        mFirstNameInput = (EditText) this.findViewById(R.id.firstNameInput);
        mLastNameInput = (EditText) this.findViewById(R.id.lastNameInput);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDBRoot = FirebaseDatabase.getInstance().getReference();
    }


    public void onConfirmRegister(View button){
        //process messages
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
        //make a new user with the email and password inputs
        //do nothing if any field is empty

        if ( (mEmailInput.getText().toString().length() * mPasswordInput.getText().toString().length()
                * mFirstNameInput.getText().toString().length() * mLastNameInput.getText().toString().length()) > 0) {
            (firebaseAuth.createUserWithEmailAndPassword(mEmailInput.getText().toString().trim(), mPasswordInput.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                String userID = firebaseAuth.getCurrentUser().getUid();
                                String firstName = mFirstNameInput.getText().toString();
                                String lastName = mLastNameInput.getText().toString();
                                String email = firebaseAuth.getCurrentUser().getEmail();
                                //write to Firebase Database
                                //create a new child of the users directory for each userID
                                firebaseDBRoot.child("Users").child(userID).setValue(new User(firstName, lastName, email));

                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();

                                //go to login activity
                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                //Log exception
                                Log.d("ERROR", task.getException().toString());
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

        else{
            progressDialog.dismiss();
            Toast.makeText(RegistrationActivity.this, "Please enter something in for all fields", Toast.LENGTH_LONG).show();
        }
    }


}
