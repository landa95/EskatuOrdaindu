package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eus.ilanda.eskatuetaordaindu.manager.DBManager;

public class ClientActivity extends AppCompatActivity {

    Button signOut;

    private DBManager dbManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setUpControls();
    }

    public void setUpControls(){
        signOut = (Button) findViewById(R.id.btn_Client_SignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.signOut(ClientActivity.this);
            }
        });
    }

    public static Intent createIntent(Context context) {
        Intent in  = new Intent();
        in.setClass(context, ClientActivity.class);
        return in;
    }
}
