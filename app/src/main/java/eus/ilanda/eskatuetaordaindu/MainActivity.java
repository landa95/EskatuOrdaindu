package eus.ilanda.eskatuetaordaindu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;


public class MainActivity extends AppCompatActivity {

    //Sign in button
    Button btnsignIn;
    TextView dataTextview;
    Button btnTest;


    //Authentication manager
    FirebaseAuth auth ;
    private DBManager dbManager = new DBManager();



    private static final int RC_SIGN_IN= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            dbManager.userType(auth.getUid(),this);
            Log.w("MAIN NOT NULL", Boolean.toString(auth.getCurrentUser()== null)+ " is null?");
        }else {
            setContentView(R.layout.activity_main);
            setUpControls();
        }
    }// protected void onCreate(Bundle savedInstanceState)

    private void setUpControls(){ 
        //Button sign in
        btnsignIn = (Button)findViewById(R.id.btnSignIn);
        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                ArrayList<AuthUI.IdpConfig> selectedProviders = new ArrayList<AuthUI.IdpConfig>();
                selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder().build());
                selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().setSignInOptions(gso).build());
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(true).setAvailableProviders(selectedProviders).build(), RC_SIGN_IN);
                }
        });


        //Testing
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbManager
                ItemMenu item = new ItemMenu("ghjkl", "Pasta Carbonara", "MAcarrones, tomate, carne de vacuno", 10.5, "hjk");
                dbManager.newItemMenu(item);
            }
        });



    }// private void setUpControls()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);

            return;
        }

    }//protected void onActivityResult(int requestCode, int resultCode, Intent data)

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        Toast toast;

        //Successfully signed in
        if (resultCode == RESULT_OK){
            FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();

            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()){
                //Datu Basean erregistratu (owner, client)
               Log.w("LOG_IN:","New User");
               Log.w("RESPONSE", " \n email: "+ response.getEmail().toString() + "\n name: " + auth.getCurrentUser().getDisplayName().toString() + " \n providerType: " + response.getProviderType() + " uid: " + auth.getUid().toString());
               dbManager.newUser();
               dbManager.userType(auth.getUid(),this);

            }else{
                //Log-in egin, erabiltzaile motaren arabera
                Log.w("LOG_IN", "Old User");
                Log.w("RESPONSE", " \n email: "+ response.getEmail().toString() + "\n name: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString() + " \n providerType: " + response.getProviderType() + " uid: " + FirebaseAuth.getInstance().getUid().toString());
                dbManager.userType(auth.getUid(),this);


            }
            return;
        }else{
            //Sign in failed
            if (response == null){
                //User pressed back button
                toast = Toast.makeText(this, "Sign in was cancelled", Toast.LENGTH_LONG);
                toast.show();

            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK){
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();

            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                toast = Toast.makeText(this, "Unkonwn Error!", Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }//private void handleSignInResponse(int resultCode, Intent data




    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }//public static Intent createIntent(Context context)



}//public class MainActivity
