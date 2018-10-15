package eus.ilanda.eskatuetaordaindu.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eus.ilanda.eskatuetaordaindu.ClientActivity;
import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.CategoryAdapter;
import eus.ilanda.eskatuetaordaindu.adapters.ItemAdapter;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FragmentMenuView extends Fragment implements CategoryAdapter.OnItemClickListener, DBManager.CallbackCategory{

    private RecyclerView menu;

    private RecyclerView.LayoutManager mLayoutManager;

    private DBManager manager =  new DBManager(this);

    private CategoryAdapter categoryAdapter;

    private ItemAdapter itemAdapter;

    ClientActivity clientActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_viewer, container, false);
        clientActivity =(ClientActivity) getActivity();

        Toast.makeText(getContext(), Integer.toString(clientActivity.getCart().size()), Toast.LENGTH_SHORT).show();
       setUpControls(v,getContext());
        menu.setLayoutManager(mLayoutManager);
        menu.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        menu.setAdapter(categoryAdapter);
        manager.loadCategories();

        return v;
    }

    private void setUpControls(View v, final Context context) {
        menu = (RecyclerView) v.findViewById(R.id.list_menu);
        ArrayList<Category> names  = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(v.getContext());
        categoryAdapter = new CategoryAdapter(R.layout.list_category, names, this);

        ArrayList<ItemMenu> items = new ArrayList<>();
        itemAdapter = new ItemAdapter(R.layout.list_item,items , null);
    }

    @Override
    public void onItemClick(Category category, int position) {
        final android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentMenuViewItem menuViewItem = new FragmentMenuViewItem();
        menuViewItem.setCategory(category.getId());
        transaction.replace(android.R.id.tabcontent, menuViewItem);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemLongClick(Category category, int position) {

    }

    @Override
    public void updateCategoryAdapter(List<Category> categories) {
        categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();
    }



}
