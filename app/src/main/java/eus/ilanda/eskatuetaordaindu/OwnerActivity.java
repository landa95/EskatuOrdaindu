package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OwnerActivity extends AppCompatActivity {

    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        setUpControls();
    }

    public void setUpControls(){
        signOut = (Button) findViewById(R.id.btn_Owner_SignOut);
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
                    startActivity(MainActivity.createIntent(OwnerActivity.this));
                }else{ //sign out failed
                    Toast.makeText(OwnerActivity.this, "Sign out failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public static Intent createIntent(Context context) {
        Intent in  = new Intent();
        in.setClass(context, OwnerActivity.class);
        return in;
    }
}
