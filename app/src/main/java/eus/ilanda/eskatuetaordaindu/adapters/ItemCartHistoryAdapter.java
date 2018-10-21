package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class ItemCartHistoryAdapter extends RecyclerView.Adapter<ItemCartHistoryAdapter.ViewHolder> {

    private int layout;
    private List<OrderItem> orderItems;

    public ItemCartHistoryAdapter (int layout, List<OrderItem> items){
        this.layout = layout;
        this.orderItems = items;
    }
    @Override
    public ItemCartHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ItemCartHistoryAdapter.ViewHolder vh = new ItemCartHistoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemCartHistoryAdapter.ViewHolder holder, int position) {
        holder.bind(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return this.orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

       private TextView itemName, itemQuantity, itemPrice;

        public ViewHolder(View v){
            super(v);
            this.itemName = v.findViewById(R.id.txt_history_item_name);
            this.itemQuantity = v.findViewById(R.id.txt_history_quantity);
            this.itemPrice = v.findViewById(R.id.txt_history_item_price);

        }

        public void bind(OrderItem item ){
            itemName.setText(item.getItem().getItemName().toString());
            itemQuantity.setText("x "+Integer.toString(item.getQuantity()));
            itemPrice.setText("€ "+Double.toString(item.getQuantity() * item.getItem().getPrize()));
        }

    }
}
