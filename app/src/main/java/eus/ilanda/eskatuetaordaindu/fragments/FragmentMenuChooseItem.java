package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;
import eus.ilanda.eskatuetaordaindu.models.User;

public class FragmentMenuChooseItem extends Fragment implements DBManager.CallbackUser{


    private  ImageView imageView;

    private TextView itemName, itemDescription, itemPrize, itemQuantity;
    private ImageButton more, less;
    private OrderItem orderItem = new OrderItem();
    private Button addToCart;

    private ImageView favourite;
    private int favouriteTag = 0;

    private String imageURL ="";

    private ClientActivity activity;

    private DBManager manager = new DBManager(this);

    private User user;
    private ItemMenu item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_choose_item, container,false);
        Bundle bundle = this.getArguments();
        item = bundle.getParcelable("item");
        manager.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());

        this.orderItem.setItem(item);

        setUpControls(v);
        activity = (ClientActivity) getActivity();

        boolean exists = false;
        int position = 0;


        for (int i =0; i< activity.getCart().size();i++ ){
            if (activity.getCart().get(i).getItem().getId().equals(orderItem.getItem().getId())){
                exists = true;
                position = i;
            }
        }
        Log.w("MISTERIOBAT", Boolean.toString(exists) + " Position" + Integer.toString(position) );
        if (exists == true){
            this.orderItem = activity.getCart().get(position);
            itemQuantity.setText(Integer.toString(orderItem.getQuantity()));
            itemPrize.setText(Double.toString(orderItem.getQuantity() * orderItem.getItem().getPrize()));
        }else {
            this.orderItem.setItem(item);
        }
       imageURL = item.getImageURL();
       itemName.setText(item.getItemName());
       itemDescription.setText(item.getItemDetails());
       itemPrize.setText(Double.toString(item.getPrize()));

       loadImageWithPicasso();
        return v;
    }

    public void loadImageWithPicasso()
    {
        Picasso.get()
                .load(imageURL)
                //.resize(size.x, 200)
                .fit()
                .into(imageView);
    }

    private void setUpControls(View v) {
        imageView = (ImageView) v.findViewById(R.id.img_item_choose);
        favourite = (ImageView) v.findViewById(R.id.iv_favourite);
        //favourite.setTag(R.drawable.ic_favorite_white);
        itemName = (TextView) v.findViewById(R.id.txt_choose_item_name);
        itemDescription = (TextView) v.findViewById(R.id.txt_choose_item_description);
        itemPrize = (TextView) v.findViewById(R.id.txt_show_actual_prize);
        itemQuantity = (TextView) v.findViewById(R.id.txt_item_quantity) ;

        less = (ImageButton) v.findViewById(R.id.btn_less);
        more = (ImageButton) v.findViewById(R.id.btn_more);

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 1;
                i = Integer.parseInt(itemQuantity.getText().toString());
                if (i > 1){
                    i--;
                    itemQuantity.setText(Integer.toString(i));
                    setItemPrize();
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(itemQuantity.getText().toString());
                if (i >= 1){
                    i++;
                    itemQuantity.setText(Integer.toString(i));
                    setItemPrize();
                }
            }
        });

        addToCart = (Button) v.findViewById(R.id.btn_addToCart);

        //Numeros raros
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                if (activity.getCart().size() ==0) {
                    activity.getCart().add(orderItem);
                }else {
                    boolean exists = false;
                    int position = 0;
                    for (int i =0; i< activity.getCart().size();i++ ){
                        if (activity.getCart().get(i).getItem().getId().equals(orderItem.getItem().getId())){
                            exists = true;
                            position = i;
                        }
                    }
                    if (exists == true){
                        int bi = orderItem.getQuantity();
                        activity.getCart().get(position).setQuantity(bi);
                    }else {
                        activity.getCart().add(orderItem);
                    }
                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favourite.getTag().equals(R.drawable.ic_favorite)){
                    favourite.setImageResource(R.drawable.ic_favorite_white);
                    favourite.setTag(R.drawable.ic_favorite_white);
                    user.getFavourites().remove(item.getId());
                    manager.updateUser(user);

                }else if (favourite.getTag().equals(R.drawable.ic_favorite_white)){
                    favourite.setImageResource(R.drawable.ic_favorite);
                    favourite.setTag(R.drawable.ic_favorite);
                    user.getFavourites().add(item.getId());
                    manager.updateUser(user);
                }
            }
        });
    }

    private void setFavouriteIcon(ItemMenu item) {
        if (user.getFavourites().equals(null)){
            user.setFavourites(new ArrayList<String>());
            favourite.setImageResource(R.drawable.ic_favorite_white);
            favourite.setTag(R.drawable.ic_favorite_white);
        }else if (this.user.getFavourites().size()!= 0){
            for (int i = 0; i<this.user.getFavourites().size(); i++){
                if (user.getFavourites().get(i).equals(item.getId())){
                    favourite.setImageResource(R.drawable.ic_favorite);
                    favourite.setTag(R.drawable.ic_favorite);
                }else {
                    favourite.setImageResource(R.drawable.ic_favorite_white);
                    favourite.setTag(R.drawable.ic_favorite_white);

                }
            }
        }else{
            favourite.setImageResource(R.drawable.ic_favorite_white);
            favourite.setTag(R.drawable.ic_favorite_white);
        }

    }

    private void setItemPrize(){
        double d = orderItem.getItem().getPrize();
        int i = Integer.parseInt(itemQuantity.getText().toString());
        d = i*d;
        itemPrize.setText(Double.toString(d));
    }



    @Override
    public void getUser(User user) {
        this.user = user;
        setFavouriteIcon(item);
    }
}