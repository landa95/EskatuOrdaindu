package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<OrderItem> orderItems;
    private int layout;
    private CartAdapterListener listener;




    public CartAdapter(int layout, List<OrderItem> orderItems, CartAdapterListener listener){
        this.layout=layout;
        this.orderItems=orderItems;
        this.listener = listener;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(orderItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return this.orderItems.size();
    }

    public void setOrderItemsList(List<OrderItem> orderItems){
        this.orderItems = orderItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtCartName;
        private ImageButton btnLess;
        private ImageButton btnAdd;
        private TextView txtItemQuantity;
        private TextView txtItemCartPrize;
        private ImageButton btnRemoveItem;


        public ViewHolder(View v) {
            super(v);
            this.txtCartName = (TextView) v.findViewById(R.id.txt_cart_item);
            this.btnLess = (ImageButton) v.findViewById(R.id.btn_cart_less);
            this.btnAdd = (ImageButton) v.findViewById(R.id.btn_cart_add);
            this.txtItemQuantity= (TextView) v.findViewById(R.id.txt_cart_quantity);
            this.txtItemCartPrize = (TextView) v.findViewById(R.id.txt_cart_orderPrice);
            this.btnRemoveItem =(ImageButton)v.findViewById(R.id.btn_cart_remove);
        }

        public void bind(final OrderItem orderItem, final CartAdapterListener listener){
            txtCartName.setText(orderItem.getItem().getItemName().toString());
            txtItemQuantity.setText(Integer.toString(orderItem.getQuantity()));
            txtItemCartPrize.setText("€" +Double.toString(orderItem.getQuantity() * orderItem.getItem().getPrize()));

            btnLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = 1;
                    i = Integer.parseInt(txtItemQuantity.getText().toString());
                    if (i > 1) {
                        i--;
                        orderItem.setQuantity(i);
                        txtItemQuantity.setText(Integer.toString(i));
                        double d = orderItem.getItem().getPrize();
                        d = i * d;
                        txtItemCartPrize.setText("€"+ Double.toString(d));
                        listener.updateTotalPrize();
                    }

                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = 1;
                    i = Integer.parseInt(txtItemQuantity.getText().toString());
                    if (i >= 1) {
                        i++;
                        txtItemQuantity.setText(Integer.toString(i));
                        orderItem.setQuantity(i);
                        double d = orderItem.getItem().getPrize();
                        d = i * d;
                        txtItemCartPrize.setText("€ "+Double.toString(d));
                        listener.updateTotalPrize();
                    }

                }
            });

            this.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.removeItem(orderItem, getAdapterPosition());
                }
            });
        }


        @Override
        public String toString() {
            return super.toString();
        }
    }

    public interface CartAdapterListener{
        void removeItem(OrderItem orderItem, int position);
        void updateTotalPrize();
    }
}
