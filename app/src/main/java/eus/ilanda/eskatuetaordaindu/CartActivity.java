package eus.ilanda.eskatuetaordaindu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.adapters.CartAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class CartActivity extends AppCompatActivity  implements CartAdapter.CartAdapterListener{

    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnOrder;
    private EditText txtTableNumber;

    private Order order = new Order();

    private ArrayList<OrderItem> cart = new ArrayList<>();

    private ClientActivity activity;

    private DBManager manager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

       cart = getIntent().getParcelableArrayListExtra("cart");


        setUpControls();
    }

    private void setUpControls() {
        recyclerView = (RecyclerView) findViewById(R.id.list_cart_items);
        layoutManager= new LinearLayoutManager(this);
        cartAdapter = new CartAdapter(R.layout.list_cart, cart,this);

        btnOrder = findViewById(R.id.btn_cart_order);
        txtTableNumber = findViewById(R.id.et_num_table);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(cartAdapter);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTableNumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter the table number", Toast.LENGTH_SHORT).show();
                }else{
                    order.setTableNumber(Integer.parseInt(txtTableNumber.getText().toString()));
                    order.setOrderItems(cart);
                    order.setPaid(true);
                    order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    order.setDetails("details");
                    manager.addOrder(order);
                }
            }
        });
    }


    public static Intent createIntent(Context context){
        Intent in = new Intent();
        in.setClass(context, CartActivity.class);
        return in;
    }//public static Intent createIntent(Context context)

    @Override
    public void removeItem(OrderItem orderItem, int position) {
        cart.remove(orderItem);
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTotalPrize() {

    }


        @Override
    public boolean onSupportNavigateUp() {
        //pass data to client activity
       Intent returnData = new Intent();
        returnData.setClass(this, ClientActivity.class);
        returnData.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        returnData.putParcelableArrayListExtra("cart", cart);
        setResult(Activity.RESULT_OK, returnData);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }
}
