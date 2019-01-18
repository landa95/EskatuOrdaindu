package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FavouriteAdapter  extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{

    private int layout;
    private List<ItemMenu> itemsMenus;
    final CallbackFavouriteAdapter callbackFavouriteAdapter;

    public FavouriteAdapter(int layout, List<ItemMenu> items, CallbackFavouriteAdapter favouriteAdapterCallback ){
        this.layout = layout;
        this.itemsMenus = items;
        this.callbackFavouriteAdapter = favouriteAdapterCallback;
    }

    public void setItemsMenus(List<ItemMenu> itemsMenus){
        this.itemsMenus = itemsMenus;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
       ViewHolder vh = new ViewHolder(v);
       return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(itemsMenus.get(position), this.callbackFavouriteAdapter);


    }

    @Override
    public int getItemCount() {
        return this.itemsMenus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fav_item_name;
        private TextView fav_item_details;
        private TextView fav_item_prize;
        private ImageButton isFavourite;

        public ViewHolder(View v){
            super(v);
            this.fav_item_name= v.findViewById(R.id.txt_fav_item_name);
            this.fav_item_details = v.findViewById(R.id.txt_fav__item_details);
            this.fav_item_prize = v.findViewById(R.id.txt__fav_item_prize);
            this.isFavourite = v.findViewById(R.id.btnFavourite);

        }

        public void bind(final ItemMenu itemMenu, final CallbackFavouriteAdapter callbackFavouriteAdapter){
            fav_item_name.setText(itemMenu.getItemName().toString());

            fav_item_details.setText(itemMenu.getItemDetails().toString());
            fav_item_prize.setText("€ "+Double.toString(itemMenu.getPrize()));

            isFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackFavouriteAdapter.clickIcon(itemMenu , getAdapterPosition());
                }
            });

            //On click item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackFavouriteAdapter.onClick(itemMenu, getAdapterPosition());
                }
            });
        }
    }

    public interface CallbackFavouriteAdapter{
        //delete from favourites
        void clickIcon(ItemMenu itemMenu, int position);
        //click
        void onClick(ItemMenu itemMenu, int position);


    }
}
