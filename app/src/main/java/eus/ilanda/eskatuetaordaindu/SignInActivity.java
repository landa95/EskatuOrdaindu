package eus.ilanda.eskatuetaordaindu;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    TextView userEmail;
    TextView userName;

    Button signOut;



    final FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();
        if(!currentUser.isEmailVerified()){
            currentUser.sendEmailVerification();
        }
        setUpControls();
    }

    private void setUpControls(){

        userEmail = (TextView) findViewById(R.id.user_email);
        userName = (TextView) findViewById(R.id.user_display_name);

        signOut = (Button) findViewById(R.id.btnSignOut);

        userEmail.setText(currentUser.getEmail().toString());
        userName.setText(currentUser.getDisplayName().toString());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(MainActivity.createIntent(SignInActivity.this));
                }else{ //sign out failed
                    Toast.makeText(SignInActivity.this, "Sign out failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public static Intent createIntent(Context context, IdpResponse idpResponse) {
        Intent in  = new Intent();
        in.setClass(context, SignInActivity.class);
        return in;
    }
}
