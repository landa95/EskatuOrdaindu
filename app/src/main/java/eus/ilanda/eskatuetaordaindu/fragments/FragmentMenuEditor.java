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

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.adapters.CategoryAdapter;
import eus.ilanda.eskatuetaordaindu.dialogs.DialogFragmentCategory;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class FragmentMenuEditor extends Fragment {

    //ListView
    private RecyclerView categoryList;

    private CategoryAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_editor, container, false);

        setUpControls(v, getContext(), savedInstanceState);
        return v;
    }

    private void setUpControls(View v, final Context context, final Bundle savedInstanceState) {
        categoryList = (RecyclerView) v.findViewById(R.id.list_categories);
        ArrayList<Category> names  = new ArrayList<Category>();

        final DBManager manager = new DBManager();


        mLayoutManager = new LinearLayoutManager(v.getContext());

        mAdapter = new CategoryAdapter(R.layout.list_category, names, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category, int position) {
                Toast.makeText(context,"Category id: " + category.getId() + " , " + category.getCategoryName() ,Toast.LENGTH_SHORT).show();
            }

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
        });
        categoryList.setLayoutManager(mLayoutManager);
        categoryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        categoryList.setAdapter(mAdapter);

        manager.loadCategories(context, mAdapter);


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
}
