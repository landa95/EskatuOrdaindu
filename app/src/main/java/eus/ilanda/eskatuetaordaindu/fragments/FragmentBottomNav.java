package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import eus.ilanda.eskatuetaordaindu.R;


/**
 * Created by landa on 06/07/2018.
         */



public class FragmentBottomNav extends android.support.v4.app.Fragment{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                      switch (item.getItemId()){
                        case R.id.fav:
                            transaction.replace(android.R.id.tabcontent, new FragmentMenuEditor());
                            transaction.commit();
                            return true;
                    }
                    return true;
                }
            };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_bottom_nav, container, false);

        BottomNavigationView navigationView = (BottomNavigationView) v.findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelected);
        menuItem.setChecked(true);

       final android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.tabcontent, new FragmentMenuEditor());
        transaction.commit();

        return v;
    }

}