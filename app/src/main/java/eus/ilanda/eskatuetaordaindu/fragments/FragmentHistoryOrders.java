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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.OrderHistoryAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class FragmentHistoryOrders extends Fragment implements DBManager.CallbackOrder {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView list_all_orders;

    private DBManager manager =  new DBManager(this);

    private OrderHistoryAdapter orderHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_history, container, false);
        setUpControls(v);


        list_all_orders.setLayoutManager(mLayoutManager);
        list_all_orders.setAdapter(orderHistoryAdapter);

        manager.getOrdersByUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return v;

    }

    private void setUpControls(View v) {
        mLayoutManager = new LinearLayoutManager(v.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        ArrayList<Order> orders = new ArrayList<Order>();
        orderHistoryAdapter = new OrderHistoryAdapter(R.layout.list_orders_history, orders);
        list_all_orders = v.findViewById(R.id.list_order_history);
    }

    @Override
    public void getOrders(ArrayList<Order> orders) {
        orderHistoryAdapter.setOrders(orders);
        orderHistoryAdapter.notifyDataSetChanged();
    }
}
