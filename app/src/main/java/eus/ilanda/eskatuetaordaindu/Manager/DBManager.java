package eus.ilanda.eskatuetaordaindu.Manager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import eus.ilanda.eskatuetaordaindu.AdminActivity;
import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.MainActivity;
import eus.ilanda.eskatuetaordaindu.OwnerActivity;
import eus.ilanda.eskatuetaordaindu.models.User;

public class DBManager {
     private static FirebaseAuth auth;
     private static FirebaseDatabase database;


     public DBManager(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
     }

     //Sign Out current user
     public void signOut(final Context context){
         AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    context.startActivity(MainActivity.createIntent(context));
                    ((Activity) context).finish();

                }else{ //sign out failed
                    Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT);
                }
             }
         });
     }

     //Create new user
     public void newUser(){
         User user = new User(auth.getCurrentUser().getUid());
         user.setEmail(auth.getCurrentUser().getEmail().toString());
         user.setName(auth.getCurrentUser().getDisplayName().toString());
         database.getReference("users").child(user.getUid()).setValue(user);
     }


    //Check what kind of user is logged inm and start activity
    public void userType(String uid,final Context context){

        final String userType = "client";
        DatabaseReference dbRef = database.getReference("users").child(uid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getPermission().isOwner() == true) {
                    ((Activity)context).startActivity(OwnerActivity.createIntent(context));

                    ((Activity)context).finish();
                }else if(user.getPermission().isAdmin() == true){
                    ((Activity)context).startActivity(AdminActivity.createIntent(context));

                    ((Activity)context).finish();

                }else if(!user.getPermission().isOwner() && !user.getPermission().isAdmin()){
                    ((Activity)context).startActivity(ClientActivity.createIntent(context));
                    ((Activity)context).finish();
                }
                Log.w("DATABASE" , "uid: " + user.getUid() + " isOwner: " + user.getPermission().isOwner() + " isAdmin: " + user.getPermission().isAdmin());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //delete user
    //CALL dbManager.deleteUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),this);
    public void deleteUser(String uid, final Context context){

         final DatabaseReference dbRef = database.getReference("users");
         Query query = dbRef.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                String key = nodeShot.getKey();
                dbRef.child(key).removeValue();
                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.w("USER_DELETE", Boolean.toString(auth.getCurrentUser()==null)+ " is null?");
                            context.startActivity(MainActivity.createIntent(context));

                            ((Activity)context).finish();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.w("USER DELETE", Boolean.toString(auth.getCurrentUser()==null)+ " is null?");
    }
}
