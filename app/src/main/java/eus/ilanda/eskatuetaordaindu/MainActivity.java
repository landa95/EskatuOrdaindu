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
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;


public class MainActivity extends AppCompatActivity {

    //Sign in button
    Button btnsignInWithmail;
    TextView dataTextview;
    Button btnSignInWithGoogle;


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
        btnsignInWithmail = (Button)findViewById(R.id.btnSignIn);
        btnsignInWithmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                ArrayList<AuthUI.IdpConfig> selectedProviders = new ArrayList<AuthUI.IdpConfig>();
                selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder().build());
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(true).setAvailableProviders(selectedProviders).build(), RC_SIGN_IN);
                }
        });


        //Testing
        btnSignInWithGoogle =  findViewById(R.id.btnTest);
        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                ArrayList<AuthUI.IdpConfig> selectedProviders = new ArrayList<AuthUI.IdpConfig>();
                selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().setSignInOptions(gso).build());
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(true).setAvailableProviders(selectedProviders).build(), RC_SIGN_IN);

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

            long signUpInterval = 3000L;
            boolean isNewUser = Math.abs(metadata.getCreationTimestamp() - metadata.getLastSignInTimestamp()) < signUpInterval;
            if (isNewUser){
                //Datu Basean erregistratu (owner, client)
               dbManager.newUser();
               dbManager.userType(auth.getUid(),this);
            }else{
                //Log-in egin, erabiltzaile motaren arabera
                dbManager.userType(auth.getUid(),this);
                boolean isTrue  = metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp();
            }
            return;
        }else{
            //Sign in failed
            if (response == null){
                //User pressed back button
                toast = Toast.makeText(this, "Sign in was cancelled", Toast.LENGTH_LONG);
                toast.show();

            }else if (response.getErrorCode() == ErrorCodes.NO_NETWORK){
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();

            }else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
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
