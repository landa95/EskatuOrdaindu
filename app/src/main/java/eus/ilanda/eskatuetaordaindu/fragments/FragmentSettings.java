package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eus.ilanda.eskatuetaordaindu.MainActivity;
import eus.ilanda.eskatuetaordaindu.OwnerActivity;
import eus.ilanda.eskatuetaordaindu.R;

/**
 * Created by landa on 04/07/2018.
 */

public class FragmentSettings extends android.support.v4.app.Fragment
{

    private Button btnSignOut;


    final FirebaseDatabase database  = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("message");

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);
        setUpControls(v);
        return v;
    }

    public void setUpControls(View v){
       btnSignOut = (Button) v.findViewById(R.id.btn_Owner_SignOut);
       btnSignOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               signOut();
           }
       });


    }

    public void signOut(){
        AuthUI.getInstance().signOut(getView().getContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(MainActivity.createIntent(getView().getContext()));
                }else{ //sign out failed
                    Toast.makeText(getView().getContext(), "Sign out failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}