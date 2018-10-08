package eus.ilanda.eskatuetaordaindu.fragments;

import android.content.Context;
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
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FragmentMenuItem extends Fragment implements ItemAdapter.OnItemClickListener, DBManager.CallbackItemMenu {

    private RecyclerView itemList;

    private Context context;

    private RecyclerView.LayoutManager mLayoutManager;

    private ItemAdapter itemAdapter;

    private DBManager manager = new DBManager(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_menu_editor, container, false);

        context= getContext();

        setUpControls(v);
        itemList.setLayoutManager(mLayoutManager);
        itemList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        itemList.setAdapter(itemAdapter);
        manager.loadItemMenus();

        return v;
    }

    private void setUpControls(View v) {
        itemList = (RecyclerView) v.findViewById(R.id.list_categories);

        mLayoutManager = new LinearLayoutManager(v.getContext());

        ArrayList<ItemMenu> items = new ArrayList<>();
        itemAdapter = new ItemAdapter(R.layout.list_item, items,this);

        //Action Button
        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Floating botton clicked " , Toast.LENGTH_SHORT).show();
                //DialogFragmentCategory dialogFragment = new DialogFragmentCategory();
                //dialogFragment.show(getFragmentManager(), "new");

            }
        });


    }

    @Override
    public void onItemClick(ItemMenu item, int position) {

    }

    @Override
    public void onItemLongClick(ItemMenu item, int position) {

    }

    @Override
    public void updateItemMenuAdapter(List<ItemMenu> menuItems) {
        itemAdapter.setItemList(menuItems);
        itemAdapter.notifyDataSetChanged();
    }
}
