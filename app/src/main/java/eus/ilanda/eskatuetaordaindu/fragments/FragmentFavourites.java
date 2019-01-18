package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.FavouriteAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.User;

public class FragmentFavourites extends Fragment implements DBManager.CallbackUser, DBManager.CallbackItemMenuList{

    private RecyclerView  favouriteList;
    private RecyclerView.LayoutManager mLayoutManager;

    private FavouriteAdapter favouriteAdapter;

    private DBManager manager = new DBManager(this, this);

   private ArrayList<ItemMenu> itemMenuArrayList = new ArrayList<>();

   User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, null);
        manager.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setUpControls(v);
        return v;
    }

    private void setUpControls(View v) {

        favouriteList = v.findViewById(R.id.list_favourites);
        mLayoutManager = new LinearLayoutManager(v.getContext());

        favouriteAdapter = new FavouriteAdapter(R.layout.list_item_favourite, itemMenuArrayList, new FavouriteAdapter.CallbackFavouriteAdapter() {
            @Override
            public void clickIcon(ItemMenu itemMenu, int position) {
                itemMenuArrayList.remove(itemMenu);
                user.getFavourites().remove(itemMenu.getId());
                manager.updateUser(user);
                favouriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClick(ItemMenu itemMenu, int position) {
                FragmentMenuChooseItem fragmentMenuChooseItem = new FragmentMenuChooseItem();
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", itemMenu);
                fragmentMenuChooseItem.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.tabcontent, fragmentMenuChooseItem);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        favouriteList.setLayoutManager(mLayoutManager);
        favouriteList.setAdapter(favouriteAdapter);
    }

    @Override
    public void getUser(User user) {
        this.user = user;
       manager.loadFavouriteItems(user.getFavourites());
    }

    @Override
    public void getFavouriteItemMenusList(List<ItemMenu> favItemMenus) {
        favouriteAdapter.setItemsMenus(favItemMenus);
        favouriteAdapter.notifyDataSetChanged();
    }
}
