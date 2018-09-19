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

import eus.ilanda.eskatuetaordaindu.Manager.DBManager;

public class AdminActivity extends AppCompatActivity {
    Button signOut;

    private DBManager dbManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setUpControls();
    }

    public void setUpControls(){
        signOut = (Button) findViewById(R.id.btn_Admin_Signout);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbManager.signOut(AdminActivity.this);
            }
        });
    }



    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, AdminActivity.class);
        return in;
    }
}
