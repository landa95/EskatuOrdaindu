package eus.ilanda.eskatuetaordaindu.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.CategoryAdapter;
import eus.ilanda.eskatuetaordaindu.dialogs.DialogFragmentCategory;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class FragmentMenuEditor extends Fragment implements DBManager.CallbackCategory, CategoryAdapter.OnItemClickListener {

    //ListView
    private RecyclerView categoryList;

    private CategoryAdapter categoryAdapter;


    private RecyclerView.LayoutManager mLayoutManager;

    private Context context ;

    private DBManager manager =  new DBManager(this);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_editor, container, false);

        this.context = getContext();
        setUpControls(v, getContext(), savedInstanceState);
        categoryList.setLayoutManager(mLayoutManager);
        categoryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        categoryList.setAdapter(categoryAdapter);
        manager.loadCategories();
        return v;
    }

    private void setUpControls(View v, final Context context, final Bundle savedInstanceState) {
        categoryList = (RecyclerView) v.findViewById(R.id.list_categories);
        ArrayList<Category> names  = new ArrayList<>();


       /* items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));
        items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));
        items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));
        items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));
        items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));
        items.add(new ItemMenu("-LNunJEyDTv43wXl3Uty", "Pasta Carbonara", "Macarrones, tomate, carne de vacuno", 10.5, "-LNunEVyBi7n0pSkdDSn"));*/


        mLayoutManager = new LinearLayoutManager(v.getContext());

        categoryAdapter = new CategoryAdapter(R.layout.list_category, names, this);

        //itemAdapter = new ItemAdapter(R.layout.list_item,items , this);

       // categoryList.setAdapter(itemAdapter);



        //Action Button
        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Floating botton clicked " , Toast.LENGTH_SHORT).show();
                DialogFragmentCategory dialogFragment = new DialogFragmentCategory();
                dialogFragment.show(getFragmentManager(), "new");

            }
        });



    }

    //implement dbManager to update  category adapter
    @Override
    public void updateCategoryAdapter(List<Category> categories) {
        categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();
    }


    //Implement Recycler view Category on click
    @Override
    public void onItemClick(Category category, int position) {
        final android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.tabcontent, new FragmentMenuItem());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    //Implement Recycler view Category on long click
    @Override
    public void onItemLongClick(final Category category, int position) {
        Toast.makeText(context,"Long Category id: " + category.getId() + " , " + category.getCategoryName() ,Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.dialog_options);
        dialog.setItems(R.array.dialog_options_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Edit Category name
                    DialogFragmentCategory dialogFragment = new DialogFragmentCategory();
                    Bundle bundle = new Bundle();
                    bundle.putString("categoryName", category.getCategoryName());
                    bundle.putString("categoryId", category.getId());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "edit");

                    //manager.updateCategory(category,new Category(20, "Update category"));
                }else if (i==1){
                    //Delete category name Pay attention to subItems
                    manager.deleteCategory(category);
                }
            }
        });
        dialog.create();
        dialog.show();
    }
/*
    //ItemMenu Adapter On Click
    @Override
    public void onItemClick(ItemMenu item, int position) {

    }

//    ItemMenu Adapter on long click

    @Override
    public void onItemLongClick(ItemMenu item, int position) {

    }

//  Item menu update adapter from DBManager
    @Override
    public void updateItemMenuAdapter(List<ItemMenu> menuItems) {
        itemAdapter.setItemList(menuItems);
        itemAdapter.notifyDataSetChanged();
    }*/
}
