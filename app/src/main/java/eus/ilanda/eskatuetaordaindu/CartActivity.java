package eus.ilanda.eskatuetaordaindu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eus.ilanda.eskatuetaordaindu.adapters.CartAdapter;
import eus.ilanda.eskatuetaordaindu.config.PaypalConfig;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class CartActivity extends AppCompatActivity  implements CartAdapter.CartAdapterListener{

    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnOrder;
    private TextView txtTotal;
    private EditText txtTableNumber;

    private Order order = new Order();

    private ArrayList<OrderItem> cart = new ArrayList<>();

    private ClientActivity activity;

    private DBManager manager = new DBManager();

    //Paypal REQUEST CODE
    public static final int PAYPAL_REQUEST_CODE = 2222;
    //Paypal configuration object set up
    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    private static String CREATE_TIME = "create_time";
    private static String RESPONSE = "response";
    private static String PAYMENT_STATE = "state";
    private static String PAYMENT_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

       cart = getIntent().getParcelableArrayListExtra("cart");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);
        order.setOrderItems(cart);
        setUpControls();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void setUpControls() {
        recyclerView = findViewById(R.id.list_cart_items);
        layoutManager= new LinearLayoutManager(this);
        cartAdapter = new CartAdapter(R.layout.list_cart, cart,this);
        txtTotal = findViewById(R.id.txt_cart_total);
        txtTotal.setText("€ "+ Double.toString(order.calculateTotalPrice()));
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
                    order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    proccessPayment();
                }
            }
        });
    }

    private void proccessPayment(){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(order.calculateTotalPrice())), "EUR",
                "Eskatu Ordaindu", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation!= null){
                    try{
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                        order.setTimestamp(getCurrentDateTime());
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = confirmation.toJSONObject();
                        JSONObject response = jsonObject.getJSONObject(RESPONSE);
                        order.setTimestamp(response.getString(CREATE_TIME));

                        if (response.getString(PAYMENT_STATE).equals("approved")){
                            order.setPaid(true);
                            manager.addOrder(order);
                        }

                        //Payment id not saved
                        Toast.makeText(this, "Payment accepted", Toast.LENGTH_LONG).show();
                            //PAYMENT CONFIRMED, ORDER CONFIRMED AND PAID, now SERVE
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }else if (resultCode== PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid payment", Toast.LENGTH_LONG).show();

        }
    }

    private String getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
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
        txtTotal.setText("€ "+ Double.toString(order.calculateTotalPrice()));

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