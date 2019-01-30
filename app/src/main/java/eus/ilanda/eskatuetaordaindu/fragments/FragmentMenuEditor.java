package eus.ilanda.eskatuetaordaindu.fragments;

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
import eus.ilanda.eskatuetaordaindu.dialogs.DialogFragmentEditDelete;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class FragmentMenuEditor extends Fragment implements DBManager.CallbackCategory, CategoryAdapter.OnItemClickListener {

    //ListView
    private RecyclerView categoryList;

    private CategoryAdapter categoryAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private DBManager manager =  new DBManager(this);

    private DialogFragmentCategory dialogFragmentCategory = new DialogFragmentCategory();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_editor, container, false);

        setUpControls(v, getContext());
        categoryList.setLayoutManager(mLayoutManager);
        categoryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        categoryList.setAdapter(categoryAdapter);
        manager.loadCategories();
        return v;
    }

    private void setUpControls(View v, final Context context) {
        categoryList = (RecyclerView) v.findViewById(R.id.list_categories);
        ArrayList<Category> names  = new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(v.getContext());

        categoryAdapter = new CategoryAdapter(R.layout.list_category, names, this);


        //Action Button
        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog(dialogFragmentCategory.ACTION_NEW, null);


            }
        });
    }

    public void showCategoryDialog(final String action, final Category category){
        if (action.equals(dialogFragmentCategory.ACTION_NEW)){
            dialogFragmentCategory.setCategory(new Category());
        }else if (action.equals(dialogFragmentCategory.ACTION_EDIT)){
            dialogFragmentCategory.setCategory(category);
        }

        dialogFragmentCategory.newInstance(new DialogFragmentCategory.OnDialogClick() {
            @Override
            public void onPositiveClick(Category category) {
                if (action.equals(dialogFragmentCategory.ACTION_NEW)){
                    manager.newCategory(category);
                }else if (action.equals(dialogFragmentCategory.ACTION_EDIT)){
                    manager.updateCategory(category);
                }
            }

            @Override
            public void onNegativeClick() {

            }
        });
        dialogFragmentCategory.show(getFragmentManager(), null);
    }

    //implement dbManager to update  category adapter
    @Override
    public void updateCategoryAdapter(List<Category> categories) {
        categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void hasSubItems(boolean hasSubItems, Category category) {
        if (hasSubItems){
            Toast.makeText(getContext(), " Unable to delete: this category has Item Menus", Toast.LENGTH_SHORT).show();
        }else {
            manager.deleteCategory(category);
    }
    }


    //Implement Recycler view Category on click
    @Override
    public void onItemClick(Category category, int position) {
        final android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentMenuItem menuItemFragment = new FragmentMenuItem();
        menuItemFragment.setCategory(category.getId());
        transaction.replace(android.R.id.tabcontent, menuItemFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    //Implement Recycler view Category on long click
    @Override
    public void onItemLongClick(final Category category, int position) {
        final DialogFragmentEditDelete dialogFragmentEditDelete = new DialogFragmentEditDelete();
        dialogFragmentEditDelete.setTitle("Item");
        dialogFragmentEditDelete.newInstance(new DialogFragmentEditDelete.OnEditDeleteClick() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Edit Category name
                    Toast.makeText(getContext(), category.getCategoryName(), Toast.LENGTH_SHORT).show();
                    showCategoryDialog(dialogFragmentCategory.ACTION_EDIT, category);

                }else if (i==1){
                    //Delete category pay attention to subItems
                    manager.hasSubItemMenu(category);
                }
            }
        });
        dialogFragmentEditDelete.show(getFragmentManager(), null);
    }

}
