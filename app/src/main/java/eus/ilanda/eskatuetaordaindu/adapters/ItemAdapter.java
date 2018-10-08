package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private int layout;
    private List<ItemMenu> items;
    private OnItemClickListener listener;

    public ItemAdapter (int layout, List<ItemMenu> items, OnItemClickListener listener){
        this.layout = layout;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void setItemList(List<ItemMenu> list){
        this.items = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;
        private TextView itemDetails;
        private TextView itemPrize;

        public ViewHolder(View v){
            super(v);
            this.itemName = (TextView) v.findViewById(R.id.txt_item_name);
            this.itemDetails  = (TextView) v.findViewById(R.id.txt_item_details);
            this.itemPrize = (TextView) v.findViewById(R.id.txt_item_prize);
        }

        public void bind(final ItemMenu item , final  OnItemClickListener listener){
            itemName.setText(item.getItemName());
            itemDetails.setText(item.getItemDetails());
            itemPrize.setText(Double.toString(item.getPrize()));

            super.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, getAdapterPosition());
                }
            });

            super.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, getAdapterPosition());
                    return true;
                }
            });
        }


    }



    public interface OnItemClickListener{
        void onItemClick(ItemMenu item, int position);
        void onItemLongClick(ItemMenu item , int position);
    }

}
