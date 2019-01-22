package eus.ilanda.eskatuetaordaindu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.RestOrderAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class FragmentRestUnServedOrders extends Fragment implements  DBManager.CallbackOrderRestaurant , RestOrderAdapter.CallbackRestClick{

    private RecyclerView list_unserved_orders;
    RecyclerView.LayoutManager layoutManager;
    RestOrderAdapter restOrderAdapter;
    private  ArrayList<Order> orders = new ArrayList<>();

    DBManager dbManager = new DBManager(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_rest_unserved, container, false);

        setUpControls(v);
        list_unserved_orders.setLayoutManager(layoutManager);
        list_unserved_orders.setAdapter(restOrderAdapter);

        //false for un-served orders
        dbManager.getOrdersInRestaurant(false);
        return v;
    }

    private void setUpControls(View v) {
        layoutManager = new LinearLayoutManager(v.getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        restOrderAdapter = new RestOrderAdapter(R.layout.list_rest_order, orders, this);
        list_unserved_orders = v.findViewById(R.id.list_unserved_orders);

    }

    @Override
    public void getOrders(ArrayList<Order> orders) {
        restOrderAdapter.setOrders(orders);
        restOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickListener(final Order order) {
       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
       builder.setTitle(getResources().getString(R.string.dialog_un_served));
       builder.setMessage(getResources().getString(R.string.dialog_un_served_question));

       builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
               order.setServed(true);
               orders.remove(order);
               restOrderAdapter.notifyDataSetChanged();
               dbManager.updateOrder(order);
           }
       });

       builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
           }
       });

       AlertDialog alertDialog = builder.create();
       alertDialog.show();

    }
}
