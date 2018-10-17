package eus.ilanda.eskatuetaordaindu.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.ItemAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FragmentMenuViewItem extends Fragment implements ItemAdapter.OnItemClickListener, DBManager.CallbackItemMenu {
    private RecyclerView menu;

    private RecyclerView.LayoutManager mLayoutManager;

    private DBManager manager =  new DBManager(this);

    private ItemAdapter itemAdapter;

    private String category = "";

    ClientActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_viewer, container, false);

        activity = (ClientActivity) getActivity();


        setUpControls(v,getContext());

        menu.setLayoutManager(mLayoutManager);
        menu.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        menu.setAdapter(itemAdapter);
        manager.loadItemsByCategory(this.category);


        return v;
    }



    private void setUpControls(View v, final Context context) {
        menu = (RecyclerView) v.findViewById(R.id.list_menu);
        mLayoutManager = new LinearLayoutManager(v.getContext());

        ArrayList<ItemMenu> items = new ArrayList<>();
        itemAdapter = new ItemAdapter(R.layout.list_item,items , this);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void updateItemMenuAdapter(List<ItemMenu> menuItems) {
        itemAdapter.setItemList(menuItems);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void uploadImage(ItemMenu item, Uri uri) {

    }

    @Override
    public void onItemClick(ItemMenu item, int position) {
        FragmentMenuChooseItem fragmentMenuChooseItem = new FragmentMenuChooseItem();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragmentMenuChooseItem.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.tabcontent, fragmentMenuChooseItem);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemLongClick(ItemMenu item, int position) {

    }
}
