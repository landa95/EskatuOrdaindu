package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eus.ilanda.eskatuetaordaindu.MainActivity;
import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.User;

/**
 * Created by landa on 04/07/2018.
 */

public class FragmentSettings extends android.support.v4.app.Fragment implements DBManager.CallbackUser
{

    private Button btnSignOut, btnAcceptChange;

    private EditText editUserName;
    private User dbuser;
    private DBManager manager = new DBManager(this);

    final FirebaseDatabase database  = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("message");

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        setUpControls(v);
        manager.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return v;
    }

    public void setUpControls(View v){
        editUserName = v.findViewById(R.id.settings_name_edit);
        btnAcceptChange = v.findViewById(R.id.btn_edit_name);
        btnAcceptChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editUserName.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Enter the user name" , Toast.LENGTH_SHORT).show();
                }else{
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(editUserName.getText().toString()).build();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dbuser.setName(editUserName.getText().toString());
                                Toast.makeText(getContext(), "User display name updated", Toast.LENGTH_SHORT).show();
                                manager.updateUser(dbuser);
                            }
                        }
                    });
                }
            }
        });

       btnSignOut = (Button) v.findViewById(R.id.btn_Owner_SignOut);
       btnSignOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DBManager dbManager = new DBManager();
               dbManager.deleteUser(auth.getCurrentUser().getUid(), getContext());
           }
       });
    }

    @Override
    public void getUser(User user) {
        dbuser = user;
    }
}