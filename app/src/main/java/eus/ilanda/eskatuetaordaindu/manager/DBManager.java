package eus.ilanda.eskatuetaordaindu.manager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.AdminActivity;
import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.MainActivity;
import eus.ilanda.eskatuetaordaindu.OwnerActivity;
import eus.ilanda.eskatuetaordaindu.models.Category;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.User;

public class DBManager {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    private CallbackCategory categoryCallbackListener;
    private CallbackCategoryClient callbackCategoryClient;
    private CallbackItemMenu callbackItemMenuListener;
    private CallbackUser callbackUser;
    private CallbackItemMenuList callbackItemMenuList;
    private CallbackOrderRestaurant callbackOrderRestaurant;

    private CallbackOrder callbackOrder;

    public  interface CallbackOrderRestaurant{
        void getOrders(ArrayList<Order> orders);
    }

    public interface CallbackCategory {
        void updateCategoryAdapter(List<Category> categories);
        void hasSubItems(boolean bool, Category cat);
    }


    public interface CallbackCategoryClient {
        void updateCategoryAdapter(List<Category> categories);
    }

    public interface CallbackItemMenu {
        void updateItemMenuAdapter(List<ItemMenu> menuItems);
        void uploadImage(ItemMenu item, Uri uri);
    }

    public interface CallbackItemMenuList{
        void getFavouriteItemMenusList(List<ItemMenu> favItemMenus);
    }

    public interface CallbackUser {
        void getUser(User user);
    }

    public interface CallbackOrder{
        void getOrders(ArrayList<Order> orders);
    }



    public DBManager(CallbackCategoryClient callbackCategoryClient){
        this.callbackCategoryClient = callbackCategoryClient;
    }

    public DBManager (CallbackOrder callbackOrder){
        this.callbackOrder = callbackOrder;
    }

    public DBManager(CallbackCategory callbackListener) {
        this.categoryCallbackListener = callbackListener;
    }

    public DBManager(CallbackItemMenu callbackItemMenu) {
        this.callbackItemMenuListener = callbackItemMenu;
    }

    public DBManager(CallbackUser callbackUser) {
        this.callbackUser = callbackUser;
    }

    public DBManager(CallbackCategory callbackCategory, CallbackItemMenu callbackItemMenu) {
        this.categoryCallbackListener = callbackCategory;
        this.callbackItemMenuListener = callbackItemMenu;
    }

    public DBManager(CallbackItemMenuList callbackItemMenuList, CallbackUser callbackUser){
        this.callbackItemMenuList = callbackItemMenuList;
        this.callbackUser = callbackUser;
    }

    public DBManager(CallbackOrderRestaurant callbackOrderRestaurant){
        this.callbackOrderRestaurant = callbackOrderRestaurant;
    }

    public DBManager() {

    }

    //Sign Out current user
    public void signOut(final Context context) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    context.startActivity(MainActivity.createIntent(context));
                    ((Activity) context).finish();

                } else { //sign out failed
                    Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Create new user
    public void newUser() {
        User user = new User(auth.getCurrentUser().getUid());
        user.setEmail(auth.getCurrentUser().getEmail().toString());
        user.setName(auth.getCurrentUser().getDisplayName().toString());
        database.getReference("users").child(user.getUid()).setValue(user);
    }

    public void getUser(String uid) {
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

    public void getUpdatableUser(String uid) {
        DatabaseReference dbRef = database.getReference("users").child(uid);
        dbRef.addValueEventListener(new ValueEventListener() {
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
    public void userType(String uid, final Context context) {


        DatabaseReference dbRef = database.getReference("users").child(uid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getPermission().isOwner()) {
                    (context).startActivity(OwnerActivity.createIntent(context));
                    ((Activity) context).finish();
                } else if (user.getPermission().isAdmin()) {
                    ( context).startActivity(AdminActivity.createIntent(context));

                    ((Activity) context).finish();

                } else if (!user.getPermission().isOwner() && !user.getPermission().isAdmin()) {
                    ( context).startActivity(ClientActivity.createIntent(context));
                    ((Activity) context).finish();
                }
                Log.w("DATABASE", "uid: " + user.getUid() + " isOwner: " + user.getPermission().isOwner() + " isAdmin: " + user.getPermission().isAdmin());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //delete user
    //CALL dbManager.deleteUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),this);
    public void deleteUser(String uid, final Context context) {
        final DatabaseReference dbRef = database.getReference("users");
        Query query = dbRef.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                final String key = nodeShot.getKey();
                try {
                    if (auth.getCurrentUser() != null) {
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
                } catch (NullPointerException e) {
                    Log.wtf("DELETE USER", "Null Pointer exception");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.w("USER DELETE", Boolean.toString(auth.getCurrentUser() == null) + " is null?");
    }

    public void loadCategories() {
        final DatabaseReference dbRef = database.getReference("menu").child("categories");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Category> categoryList = new ArrayList<Category>();

                Log.w("DATABASE", "database");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categoryList.add(category);
                }
                if (categoryCallbackListener == null){
                    callbackCategoryClient.updateCategoryAdapter(categoryList);
                }else {
                    categoryCallbackListener.updateCategoryAdapter(categoryList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void newCategory(final Category category) {
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

    public void deleteCategory(Category category) {

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

    public boolean hasSubItemMenu(final Category category)
    {
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        Query query = dbRef.orderByChild("category").equalTo(category.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getChildren().iterator().hasNext()){
                   categoryCallbackListener.hasSubItems(false, category);
                }else{
                    categoryCallbackListener.hasSubItems(true, category);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    public void updateCategory(final Category category) {
        final DatabaseReference dbRef = database.getReference("menu").child("categories");
        Category newCategory = category;
        HashMap<String, Object> update = new HashMap<>();
        update.put(category.getId(), category);
        dbRef.child(newCategory.getId()).setValue(newCategory);
    }

    public void newItemMenu(final ItemMenu item) {
        if (item.getImageURL() == null) {
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
    public void newItemMenu(final ItemMenu item, final Uri uri) {
        if (item.getImageURL() == null) {
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


    public void loadItemMenus() {
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ItemMenu> itemList = new ArrayList<ItemMenu>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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

    public void loadItemsByCategory(final String categoryId) {
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        Query query = dbRef.orderByChild("category").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ItemMenu> itemList = new ArrayList<ItemMenu>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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

    //
    public void loadFavouriteItems(final ArrayList<String> favourites){
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        final ArrayList<ItemMenu> itemMenus  = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ItemMenu itemMenu = snapshot.getValue(ItemMenu.class);
                        for (int i = 0; i< favourites.size(); i++){
                            if (itemMenu.getId().equals(favourites.get(i))){
                                itemMenus.add(itemMenu);
                            }
                        }
                    }
                    callbackItemMenuList.getFavouriteItemMenusList(itemMenus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateItem(final ItemMenu editItem) {
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        ItemMenu newItemMenu = editItem;
        HashMap<String, Object> update = new HashMap<>();
        update.put(editItem.getId(), newItemMenu);
        dbRef.updateChildren(update);
    }

    public void updateOrder(final Order order){
        final DatabaseReference dbRef =database.getReference("orders");
        Order newOrder  = order;
        HashMap<String, Object> update = new HashMap<>();
        update.put(order.getOrderId(), newOrder);
        dbRef.updateChildren(update);
    }

    public void deleteItem(final ItemMenu item) {
        final DatabaseReference dbRef = database.getReference("menu").child("menuItems");
        Query query = dbRef.orderByChild("id").equalTo(item.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                String key = nodeShot.getKey();
                dbRef.child(key).removeValue();
                deleteItemImage(item);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteItemImage(final ItemMenu item) {
        StorageReference fsRef = FirebaseStorage.getInstance().getReference().child("/" + item.getId());
        StorageReference imageRef = fsRef.child("image.jpg");
        imageRef.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("IMAGE", "Unable to delete item image: " + item.getId());
            }
        });
    }


    public void updateUser(User user) {
        final DatabaseReference dbRef = database.getReference("users");
        User newUser = user;
        HashMap<String, Object> update = new HashMap<>();
        update.put(user.getUid(), newUser);
        dbRef.child(user.getUid()).setValue(user);
        callbackUser.getUser(newUser);

    }

    public void addOrder(Order order) {
        DatabaseReference dbRef = database.getReference("orders");
        String key = dbRef.push().getKey();
        order.setOrderId(key);
        dbRef.child(order.getOrderId()).setValue(order);

    }

    public void getOrdersByUser(String uid){
        final DatabaseReference dbRef = database.getReference("orders");
        Query query = dbRef.orderByChild("userId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> orders = new ArrayList<Order>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    Order order = snapshot.getValue(Order.class);
                    orders.add(order);
                }
                callbackOrder.getOrders(orders);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getOrdersInRestaurant(final boolean isServed){
        final DatabaseReference dbRef = database.getReference("orders");
        Query query = dbRef.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Order order = snapshot.getValue(Order.class);
                    if (order.isServed() == isServed){
                        orders.add(order);
                        //update unserved Adapter
                    }
                }
                callbackOrderRestaurant.getOrders(orders);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
