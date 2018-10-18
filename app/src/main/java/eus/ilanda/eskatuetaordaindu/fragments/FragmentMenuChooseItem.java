package eus.ilanda.eskatuetaordaindu.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class FragmentMenuChooseItem extends Fragment {

    private  ImageView imageView;

    private TextView itemName, itemDescription, itemPrize, itemQuantity;
    private ImageButton more, less;
    private OrderItem orderItem = new OrderItem();
    private Button addToCart;

    private String imageURL ="";

    private ClientActivity activity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_choose_item, container,false);
        setUpControls(v);

       Bundle bundle = this.getArguments();
       ItemMenu item = bundle.getParcelable("item");
       this.orderItem.setItem(item);

       imageURL = item.getImageURL();
       itemName.setText(item.getItemName());
       itemDescription.setText(item.getItemDetails());
       itemPrize.setText(Double.toString(item.getPrize()));

       loadImageWithPicasso();

       activity = (ClientActivity) getActivity();
        return v;
    }

    public void loadImageWithPicasso()
    {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Picasso.get()
                .load(imageURL)
                //.resize(size.x, 200)
                .fit()
                .into(imageView);
    }

    private void setUpControls(View v) {
        imageView = (ImageView) v.findViewById(R.id.img_item_choose);

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

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                activity.getCart().add(orderItem);

            }
        });
    }

    private void setItemPrize(){
        double d = orderItem.getItem().getPrize();
        int i = Integer.parseInt(itemQuantity.getText().toString());
        d = i*d;
        itemPrize.setText(Double.toString(d));
    }
}
