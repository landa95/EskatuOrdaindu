package eus.ilanda.eskatuetaordaindu.manager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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
import eus.ilanda.eskatuetaordaindu.models.Category;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.User;

public class DBManager {
     private static FirebaseAuth auth = FirebaseAuth.getInstance();
     private static FirebaseDatabase database = FirebaseDatabase.getInstance();
     private CallbackCategory categoryCallbackListener;
     private CallbackItemMenu callbackItemMenuListener;
     private CallbackUser callbackUser;


     public interface CallbackCategory{
         void updateCategoryAdapter(List<Category> categories);
     }

     public interface CallbackItemMenu{
         void updateItemMenuAdapter(List<ItemMenu> menuItems);
         void uploadImage(ItemMenu item, Uri uri);
     }

     public interface CallbackUser{
         void getUser(User user);
    }


     public DBManager(CallbackCategory callbackListener){
         this.categoryCallbackListener = callbackListener;
     }

     public DBManager( CallbackItemMenu callbackItemMenu){
         this.callbackItemMenuListener = callbackItemMenu;
     }

     public DBManager(CallbackUser callbackUser){
        this.callbackUser = callbackUser;
     }

     public DBManager(CallbackCategory callbackCategory, CallbackItemMenu callbackItemMenu){
         this.categoryCallbackListener = callbackCategory;
         this.callbackItemMenuListener = callbackItemMenu;
     }

     public DBManager(){

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

     public void getUser(String uid){
         DatabaseReference dbRef = database.getReference("users").child(uid);
         dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                 callbackUser.getUser(user);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
     }

    //Check what kind of user is logged inm and start activity
    public void userType(String uid,final Context context){

        final String userType = "client";
        DatabaseReference dbRef = database.getReference("users").child(uid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getPermission().isOwner()) {
                    ((Activity)context).startActivity(OwnerActivity.createIntent(context));

                    ((Activity)context).finish();
                }else if(user.getPermission().isAdmin()){
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

                try {
                    if(auth.getCurrentUser() != null) {
                        auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.w("USER_DELETE", Boolean.toString(auth.getCurrentUser() == null) + " is null?");
                                    context.startActivity(MainActivity.createIntent(context));
                                    dbRef.child(key).removeValue();
                                    ((Activity) context).finish();
                                }
                            }
                        });
                    }
                }catch (NullPointerException e){
                    Log.wtf("DELETE USER" , "Null Pointer exception");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.w("USER DELETE", Boolean.toString(auth.getCurrentUser()==null)+ " is null?");
    }

    public void loadCategories() {
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
                 }
                 categoryCallbackListener.updateCategoryAdapter(categoryList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void newCategory(final Category category){
         Log.w("CATEGORY_NEW", category.getCategoryName());
         final DatabaseReference dbRef = database.getReference("menu").child("categories");
         String key = dbRef.push().getKey();
         category.setId(key);
         dbRef.child(key).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {

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

    }

    public void updateCategory(final Category category){
        final DatabaseReference dbRef = database.getReference("menu").child("categories");
        Category newCategory = category;
        HashMap<String, Object> update = new HashMap<>();
        update.put(category.getId(), category);
        dbRef.child(newCategory.getId()).setValue(newCategory);
    }

    public void newItemMenu(final ItemMenu item){
         if(item.getImageURL() == null){
             item.setImageURL("");
         }
         final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
         final String str = dbRef.push().getKey();
         item.setId(str);
         dbRef.child(str).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {

             }
         });
     }

    //DATABASE UPLOAD IMAGE
    public void newItemMenu(final ItemMenu item,final Uri uri){
        if(item.getImageURL() == null){
            item.setImageURL("");
        }

        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        final String str = dbRef.push().getKey();
        item.setId(str);

        dbRef.child(str).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callbackItemMenuListener.uploadImage(item, uri);
            }
        });
    }




    public void loadItemMenus(){
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ItemMenu> itemList = new ArrayList<ItemMenu>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    ItemMenu item = snapshot.getValue(ItemMenu.class);
                    itemList.add(item);
                }
                callbackItemMenuListener.updateItemMenuAdapter(itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

     public void loadItemsByCategory(final String categoryId){
         final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
         Query query = dbRef.orderByChild("category").equalTo(categoryId);
         query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 List<ItemMenu> itemList = new ArrayList<ItemMenu>();
                 for (DataSnapshot snapshot: dataSnapshot.getChildren())
                 {
                     ItemMenu item = snapshot.getValue(ItemMenu.class);
                     itemList.add(item);
                 }
                 callbackItemMenuListener.updateItemMenuAdapter(itemList);
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }

    public void updateItem(final ItemMenu editItem){
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        ItemMenu newItemMenu = editItem;
        HashMap<String, Object> update = new HashMap<>();
        update.put(editItem.getId(),newItemMenu );
        dbRef.child(editItem.getId()).setValue(newItemMenu);
    }

    public void deleteItem(final ItemMenu item ){
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        Query query = dbRef.orderByChild("id").equalTo(item.getId());
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
    }


}
