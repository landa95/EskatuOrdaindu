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
import eus.ilanda.eskatuetaordaindu.adapters.ItemAdapter;
import eus.ilanda.eskatuetaordaindu.dialogs.DialogFragmentEditDelete;
import eus.ilanda.eskatuetaordaindu.dialogs.DialogFragmentItem;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FragmentMenuItem extends Fragment implements ItemAdapter.OnItemClickListener, DBManager.CallbackItemMenu {

    private RecyclerView itemList;

    private Context context;

    private RecyclerView.LayoutManager mLayoutManager;

    private ItemAdapter itemAdapter;

    private DBManager manager = new DBManager(this);

    private FloatingActionButton fab;

    private String category = "";

    private  DialogFragmentItem dialogFragmentItem = new DialogFragmentItem();

    public void setCategory(String category){
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_menu_editor, container, false);

        context= getContext();

        setUpControls(v);
        itemList.setLayoutManager(mLayoutManager);
        itemList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        itemList.setAdapter(itemAdapter);

        manager.loadItemsByCategory(this.category);
        //manager.loadItemMenus();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Floating botton clicked " , Toast.LENGTH_SHORT).show();
                showItemDialog(dialogFragmentItem.ACTION_NEW, null);


            }
        });

        return v;
    }

    private void setUpControls(View v) {
        itemList = (RecyclerView) v.findViewById(R.id.list_categories);

        mLayoutManager = new LinearLayoutManager(v.getContext());

        ArrayList<ItemMenu> items = new ArrayList<>();
        itemAdapter = new ItemAdapter(R.layout.list_item, items,this);

        //Action Button
        fab= v.findViewById(R.id.fab);


    }

    public void showItemDialog(final String action, final ItemMenu item){

        if (action.equals(dialogFragmentItem.ACTION_NEW)){
            dialogFragmentItem.setItem(new ItemMenu());
        }else if (action.equals(dialogFragmentItem.ACTION_EDIT)){
            dialogFragmentItem.setItem(item);
        }

        this.dialogFragmentItem.newInstance(new DialogFragmentItem.OnDialogClick() {
            @Override
            public void onPositiveClick(ItemMenu dialogItem) {
                if(action.equals(dialogFragmentItem.ACTION_NEW)){
                    dialogItem.setCategory(category);
                    manager.newItemMenu(dialogItem);
                    Toast.makeText(getContext(), dialogItem.getItemName() , Toast.LENGTH_SHORT).show();
                }else if (action.equals(dialogFragmentItem.ACTION_EDIT)){
                    manager.updateItem(dialogItem);
                }
            }

            @Override
            public void onNegativeClick() {

            }
        });
        dialogFragmentItem.show(getFragmentManager(), null);
    }

    @Override
    public void onItemClick(ItemMenu item, int position) {

    }

    @Override
    public void onItemLongClick(final ItemMenu listItem, int position) {
        DialogFragmentEditDelete dialogFragmentEditDelete = new DialogFragmentEditDelete();
        dialogFragmentEditDelete.setTitle("Item");
        dialogFragmentEditDelete.newInstance(new DialogFragmentEditDelete.OnEditDeleteClick() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Edit Item
                    showItemDialog(dialogFragmentItem.ACTION_EDIT, listItem);
                }else if (i==1){
                    //Delete item
                    manager.deleteItem(listItem);
                }
            }
        });
        dialogFragmentEditDelete.show(getFragmentManager(), null);
    }

    //Callback from DBManager
    @Override
    public void updateItemMenuAdapter(List<ItemMenu> menuItems) {
        itemAdapter.setItemList(menuItems);
        itemAdapter.notifyDataSetChanged();
    }
}