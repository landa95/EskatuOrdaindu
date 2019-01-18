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
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.User;

public class RestOrderAdapter extends RecyclerView.Adapter<RestOrderAdapter.ViewHolder> {

    private List<Order> orders;
    private int layout;
    public CallbackRestClick callbackRestClick;


    public RestOrderAdapter(int layout, List<Order> orders, CallbackRestClick c){
        this.layout = layout;
        this.orders = orders;
        this.callbackRestClick = c;
    }

    @Override
    public RestOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(orders.get(position), callbackRestClick);
    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }

    public void setOrders(ArrayList<Order> orders){
        this.orders = orders;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements DBManager.CallbackUser
    {

        private TextView txtOrderDate, txtOrderPrice,txtClientName, txtTableNumber;
        private RecyclerView recyclerView;
        private  RecyclerView.LayoutManager layoutManager;
        ItemCartHistoryAdapter adapter;


        public ViewHolder(View v){
            super(v);
            txtOrderDate = v.findViewById(R.id.txt_rest_order_history_date);
            txtOrderPrice = v.findViewById(R.id.txt_rest_order_history_totalPrize);
            txtClientName = v.findViewById(R.id.txt_rest_order_name);
            txtTableNumber = v.findViewById(R.id.txt_rest_table_number);
            recyclerView = v.findViewById(R.id.rest_orderItem_list_history);
            layoutManager = new LinearLayoutManager(v.getContext());

        }

        public void bind(final Order order, final CallbackRestClick callbackRestClick){
            String timestamp = order.getTimestamp();
            String[] dateTime = timestamp.split("T|Z|' ' ");
            txtOrderDate.setText(dateTime[0]);
            adapter =  new ItemCartHistoryAdapter(R.layout.list_history_order_items, order.getOrderItems());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            //txtOrderDate.setText(order.getTimestamp().toString());
            txtOrderPrice.setText("€ "+ Double.toString(order.calculateTotalPrice()));
            txtTableNumber.setText("Table : " + Integer.toString(order.getTableNumber()));

            DBManager manager = new DBManager(this);
            manager.getUser(order.getUserId());

            super.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackRestClick.onClickListener(order);
                }
            });

        }

        @Override
        public void getUser(User user) {
            txtClientName.setText(user.getName().toString());
        }
    }

   public interface CallbackRestClick{
       void onClickListener(Order order);
    }
}
