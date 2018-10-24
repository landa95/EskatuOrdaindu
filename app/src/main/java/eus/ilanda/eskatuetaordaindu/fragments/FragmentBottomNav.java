package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Permission;
import eus.ilanda.eskatuetaordaindu.models.User;


/**
 * Created by landa on 06/07/2018.
         */



public class FragmentBottomNav extends android.support.v4.app.Fragment implements DBManager.CallbackUser{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                      switch (item.getItemId()){
                        case R.id.bottom_nav_owner_menu_edit: //Owner menu editor
                            popBackStack();
                            transaction.replace(android.R.id.tabcontent, new FragmentMenuEditor());
                            transaction.commit();
                            return true;
                          case R.id.restaurant_client:
                              popBackStack();
                              transaction.replace(android.R.id.tabcontent, new FragmentMenuView());
                              transaction.commit();
                              return true;

                          case R.id.bottom_nav_client_favourite:
                              popBackStack();
                               transaction.replace(android.R.id.tabcontent, new FragmentFavourites());
                              transaction.commit();
                              return true;
                          case R.id.bottom_nav_history:
                              popBackStack();
                              transaction.replace(android.R.id.tabcontent, new FragmentHistoryOrders());
                              transaction.commit();
                              return true;

                    }
                    return true;
                }
            };

    /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                      switch (item.getItemId()){
                        case R.id.fav:
                            transaction.replace(android.R.id.tabcontent, new FragmentMenuEditor());
                            transaction.commit();
                            return true;
                          case R.id.restaurant_client:
                              transaction.replace(android.R.id.tabcontent, new FragmentMenuView());
                              transaction.commit();
                    }
return true;*/
    private BottomNavigationView navigationView;

    //Show view to user
    private Permission userPersmission = new Permission();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.activity_bottom_nav, container, false);

        navigationView = v.findViewById(R.id.bottom_navigation);
        DBManager dbManager = new DBManager(this);

        dbManager.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());


        return v;
    }

    public void popBackStack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i< fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }
    }

    @Override
    public void getUser(User user) {
        this.userPersmission = user.getPermission();
        if (this.userPersmission.isOwner()){
            navigationView.inflateMenu(R.menu.bottom_nav_view_owner);
            getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, new FragmentMenuEditor()).commit();
        }else if (this.userPersmission.isAdmin()){

        }else {
            navigationView.inflateMenu(R.menu.bottom_nav_view_client);
            getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, new FragmentMenuView()).commit();
        }

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        navigationView.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelected);
        menuItem.setChecked(true);

    }
}