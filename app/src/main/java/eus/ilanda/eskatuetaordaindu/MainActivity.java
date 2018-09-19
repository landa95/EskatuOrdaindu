package eus.ilanda.eskatuetaordaindu;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.models.Client;
import eus.ilanda.eskatuetaordaindu.models.User;


public class MainActivity extends AppCompatActivity {

    //DB button onn click, write
    Button dbButton;
    //Sign in button
    Button btnsignIn;
    TextView dataTextview;

    final FirebaseDatabase database  = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("message");

    FirebaseAuth auth = FirebaseAuth.getInstance();


    private static final int RC_SIGN_IN= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpControls();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                dataTextview.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataTextview.setText("Error: " + databaseError.toString());
            }
        });

        if(auth.getCurrentUser() != null){
               userType(auth.getUid());
        }



    }// protected void onCreate(Bundle savedInstanceState)

    private void setUpControls(){ 

        //Data textview init
        dataTextview = (TextView) findViewById(R.id.textView);
        //db Init
        dbButton = (Button) findViewById(R.id.button);

        //Button sign in
        btnsignIn = (Button)findViewById(R.id.btnSignIn);

        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue("Hello, World!");
            }
        });

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
               Log.w("NEW:","New User");
               Log.w("RESPONSE", " \n email: "+ response.getEmail().toString() + "\n name: " + auth.getCurrentUser().getDisplayName().toString() + " \n providerType: " + response.getProviderType() + " uid: " + auth.getUid().toString());
               User user =   new User(auth.getUid());
               user.setEmail(response.getEmail().toString());
               user.setName(auth.getCurrentUser().getDisplayName().toString());
               database.getReference("users").child(user.getUid().toString()).setValue(user);
                userType(auth.getUid());

            }else{
                //Log-in egin, erabiltzaile motaren arabera
                Log.w("NEW", "Old User");
                Log.w("RESPONSE", " \n email: "+ response.getEmail().toString() + "\n name: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString() + " \n providerType: " + response.getProviderType() + " uid: " + FirebaseAuth.getInstance().getUid().toString());
                userType(auth.getUid());


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

    private void userType(String uid){

       final String userType = "client";
        DatabaseReference dbRef = database.getReference("users").child(uid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getPermission().isOwner() == true) {
                    startActivity(OwnerActivity.createIntent(MainActivity.this));

                    finish();
                }else if(user.getPermission().isAdmin() == true){
                    startActivity(AdminActivity.createIntent(MainActivity.this));

                    finish();

                }else if(!user.getPermission().isOwner() && !user.getPermission().isAdmin()){
                        startActivity(ClientActivity.createIntent(MainActivity.this));

                    finish();
                    }
                    Log.w("DATABASE" , "uid: " + user.getUid() + " isOwner: " + user.getPermission().isOwner() + " isAdmin: " + user.getPermission().isAdmin());
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }//public static Intent createIntent(Context context)



}//public class MainActivity
