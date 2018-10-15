package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class CartActivity extends AppCompatActivity {

    Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ArrayList<ItemMenu> cart = getIntent().getParcelableArrayListExtra("cart");
        for (int i = 0; i < cart.size(); i++){
            Log.i("PARCELABLE", Integer.toString(i+1) + "/" + Integer.toString(cart.size())+": " +cart.get(i).getItemName());
        }

    }

    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, CartActivity.class);
        return in;
    }//public static Intent createIntent(Context context)

}
