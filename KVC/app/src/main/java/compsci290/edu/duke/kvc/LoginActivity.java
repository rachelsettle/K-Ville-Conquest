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

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailInput;
    private EditText mPasswordInput;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailInput = (EditText) this.findViewById(R.id.emailInputLogin);
        mPasswordInput = (EditText) this.findViewById(R.id.passwordInputLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void onLoginConfirm (View button){
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);

        (firebaseAuth.signInWithEmailAndPassword(mEmailInput.getText().toString().trim(), mPasswordInput.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                            //go to profile
                            Intent i = new Intent(LoginActivity.this, Profile.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail().toString());
                            startActivity(i);
                        }

                        else {
                            Log.d("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}
