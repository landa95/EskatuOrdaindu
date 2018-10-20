package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.adapters.CartAdapter;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class CartActivity extends AppCompatActivity  implements CartAdapter.CartAdapterListener{

    private StorageReference storageReference;
    ImageView imageView;
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Order order;

    private ArrayList<OrderItem> cart = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

       cart = getIntent().getParcelableArrayListExtra("cart");
/*       ArrayList<OrderItem> groupedList = new ArrayList<OrderItem>();
       for (int i = 0; i< cart.size(); i++){
           if (groupedList.size()>0){
               for (int y = 0; y<groupedList.size(); y++){
                   if (cart.get(i).getItem().getId().equals(groupedList.get(y).getItem().getId())){
                       int quantity = groupedList.get(y).getQuantity();
                       groupedList.get(y).setQuantity(quantity + cart.get(i).getQuantity());
                   }else {
                       groupedList.add(cart.get(i));
                   }
               }
           }else {
               groupedList.add(cart.get(i));
           }
       }
       cart = groupedList;*/

        //order.setOrderItems(cart);

        setUpControls();
    }

    private void setUpControls() {


        recyclerView = (RecyclerView) findViewById(R.id.list_cart_items);
        layoutManager= new LinearLayoutManager(this);
        cartAdapter = new CartAdapter(R.layout.list_cart, cart,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(cartAdapter);


    }

    private void downloadPicture() {
       storageReference = FirebaseStorage.getInstance().getReference();
        try {
            final File localFile = File.createTempFile("images", "jpeg");;

      storageReference = storageReference.child("pizza.jpeg");
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                loadImageWithPicasso(localFile);
            }
        });
        }catch (Exception e){
            Log.i("File" , e.toString());
        }
    }

    public void loadImageWithPicasso(File f)
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
                Picasso.get()
                .load(f)
                .resize(size.x, 200)
                //.placeholder(ic_cargando)
                .into(imageView);
    }

    public void uploadImage(){

    }

    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, CartActivity.class);
        return in;
    }//public static Intent createIntent(Context context)

    @Override
    public void removeItem(OrderItem orderItem, int position) {

    }

    @Override
    public void updateTotalPrize() {

    }
}
