package eus.ilanda.eskatuetaordaindu.manager;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.AdminActivity;
import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.MainActivity;
import eus.ilanda.eskatuetaordaindu.OwnerActivity;
import eus.ilanda.eskatuetaordaindu.adapters.CategoryAdapter;
import eus.ilanda.eskatuetaordaindu.models.Category;
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
                final String key = nodeShot.getKey();

                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.w("USER_DELETE", Boolean.toString(auth.getCurrentUser()==null)+ " is null?");
                            context.startActivity(MainActivity.createIntent(context));
                            dbRef.child(key).removeValue();
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

    public void loadCategories(final Context context, final CategoryAdapter adapter) {
         final DatabaseReference dbRef = database.getReference("menu").child("categories");

         dbRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 List<Category> categoryList = new ArrayList<Category>();

                 Log.w("DATABASE","database");
                 for (DataSnapshot snapshot: dataSnapshot.getChildren())
                 {
                     Category category = snapshot.getValue(Category.class);
                     categoryList.add(category);
                     //Prueba forzar llenar lista
                 }
                adapter.setCategories(categoryList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void newCategory(final String categoryName){
         final DatabaseReference dbRef = database.getReference("menu").child("categories");
         dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Category> categoryList = new ArrayList<Category>();

                Log.w("DATABASE","database");
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Category category = snapshot.getValue(Category.class);
                    categoryList.add(category);
                }
                int id = 0;
                if(categoryList.size() > 0 ){
                    id = Integer.parseInt(categoryList.get(categoryList.size()-1).getId()) +1;
                }
                Category newCategory = new Category(id, categoryName);
                dbRef.child(newCategory.getId()).setValue(newCategory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteCategory(Category category){

        final DatabaseReference dbRef = database.getReference("menu").child("categories");
        Query query = dbRef.orderByChild("id").equalTo(category.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                String key = nodeShot.getKey();
                dbRef.child(key).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.w("USER DELETE", Boolean.toString(auth.getCurrentUser()==null)+ " is null?");
    }

    public void updateCategory(final Category category,final int i, final String newCategoryName){
        final DatabaseReference dbRef = database.getReference("menu").child("categories");

        Log.w("DIALOG-UPDATE" , "Old category" + category.getId() + " " + category.getCategoryName() + " New: " + newCategoryName);
        Category newCategory = new Category(Integer.parseInt(category.getId()), newCategoryName);
        HashMap<String, Object> update = new HashMap<>();
        update.put(category.getId(), newCategory);
        dbRef.child(Integer.toString(i)).setValue(newCategory);
       /* Query query = dbRef.orderByChild("id").equalTo(i);
         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Log.w("DIALOG-UPDATE" , "Old category" + category.getId() + " " + category.getCategoryName() + " New: " + newCategoryName);
                 Category newCategory = new Category(Integer.parseInt(category.getId()), newCategoryName);
                 HashMap<String, Object> update = new HashMap<>();
                 update.put(category.getId(), newCategory);
                 dbRef.child(Integer.toString(i)).updateChildren(update);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });*/
    }
}
