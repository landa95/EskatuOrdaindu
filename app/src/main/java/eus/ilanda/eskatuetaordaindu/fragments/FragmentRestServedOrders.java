package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.RestOrderAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class FragmentRestServedOrders extends Fragment implements DBManager.CallbackOrderRestaurant, RestOrderAdapter.CallbackRestClick{

    private RecyclerView list_served_orders;
    RecyclerView.LayoutManager layoutManager;
    RestOrderAdapter restOrderAdapter;
    private ArrayList<Order> orders = new ArrayList<>();


    DBManager dbManager = new DBManager(this);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_rest_served, container, false);

        setUpControls( v);
        list_served_orders.setLayoutManager(layoutManager);
        list_served_orders.setAdapter(restOrderAdapter);

        //true for served orders
        dbManager.getOrdersInRestaurant(true);

        return v;
    }

    private void setUpControls(View v) {
        layoutManager = new LinearLayoutManager(v.getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        restOrderAdapter = new RestOrderAdapter(R.layout.list_rest_order, orders, this);
        list_served_orders = v.findViewById(R.id.list_served_orders);

    }

    @Override
    public void getOrders(ArrayList<Order> orders) {
        restOrderAdapter.setOrders(orders);
        restOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickListener(Order order) {

    }
}
