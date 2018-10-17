package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class CartActivity extends AppCompatActivity {

    Order order = new Order();

    public static final String URI_FIREBASE_IMAGE ="" ;
    private StorageReference storageReference;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ArrayList<ItemMenu> cart = getIntent().getParcelableArrayListExtra("cart");

        //order.setOrderItems(cart);
        for (int i = 0; i < cart.size(); i++){
            Log.i("PARCELABLE", Integer.toString(i+1) + "/" + Integer.toString(cart.size())+": " +cart.get(i).getItemName());
        }

        imageView = (ImageView) findViewById(R.id.img_example);
        downloadPicture();
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

}
