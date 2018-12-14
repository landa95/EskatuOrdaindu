package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.Order;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<Order> orders;
    private int layout;


    public OrderHistoryAdapter(int layout, List<Order> orders){
        this.layout = layout;
        this.orders = orders;
    }

    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }

    public void setOrders(ArrayList<Order> orders){
        this.orders = orders;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtOrderDate, txtOrderPrice;
        private RecyclerView recyclerView;
        private  RecyclerView.LayoutManager layoutManager;
        ItemCartHistoryAdapter adapter;


        public ViewHolder(View v){
        super(v);
        txtOrderDate = v.findViewById(R.id.txt_order_history_date);
        txtOrderPrice = v.findViewById(R.id.txt_order_history_totalPrize);
        recyclerView = v.findViewById(R.id.orderItem_list_history);
        layoutManager = new LinearLayoutManager(v.getContext());

       }

        public void bind(final Order order){
            String timestamp = order.getTimestamp();
            String[] dateTime = timestamp.split("T|Z|' ' ");
            txtOrderDate.setText(dateTime[0]);
            adapter =  new ItemCartHistoryAdapter(R.layout.list_history_order_items, order.getOrderItems());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            //txtOrderDate.setText(order.getTimestamp().toString());
            txtOrderPrice.setText(Double.toString(order.calculateTotalPrice()));

        }
    }
}

