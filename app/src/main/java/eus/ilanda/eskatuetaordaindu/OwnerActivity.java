package eus.ilanda.eskatuetaordaindu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import eus.ilanda.eskatuetaordaindu.Manager.DBManager;
import eus.ilanda.eskatuetaordaindu.fragments.FragmentBottomNav;
import eus.ilanda.eskatuetaordaindu.fragments.FragmentSettings;
import eus.ilanda.eskatuetaordaindu.models.Owner;

public class OwnerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    NavigationView nav_view;

    View header_view;

    TextView text,email;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    private DBManager dbManager = new DBManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //  switch (item.getItemId()){
                    //    case 0:
                    //      return true;
                    //}
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            MainActivity.createIntent(this);
            finish();
        }else{
            setUpControls();
        }
    }


    public void setUpControls() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        header_view = nav_view.getHeaderView(0);

        //Change name and email texts from the header view
        text = (TextView) header_view.findViewById(R.id.nav_txt_name);
        email = (TextView) header_view.findViewById(R.id.nav_txt_email);

        text.setText(auth.getCurrentUser().getDisplayName().toString());

        email.setText(auth.getCurrentUser().getEmail().toString());

        //Use our  own toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentBottomNav()).commit();
        Toast.makeText(this, "On create", Toast.LENGTH_LONG);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId()){
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentBottomNav()).commit();
                Toast.makeText(this, "Settings press", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_exit:
                //dbManager.signOut(this);
                dbManager.deleteUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),this);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    public static Intent createIntent(Context context) {
        Intent in  = new Intent();
        in.setClass(context, OwnerActivity.class);
        return in;
    }
}
