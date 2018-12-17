package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.StatManager;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class FragmentRestaurantStats extends Fragment implements StatManager.CallbackStats {

    private TextView txtTopDish;
    StatManager statManager = new StatManager(this);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_stats, container, false);

        setUpControls(view);
        statManager.topDish();
        return view;
    }

    private void setUpControls(View view) {

        txtTopDish = view.findViewById(R.id.txt_stats_top_dish);

    }

    @Override
    public void topDish(String itemMenu) {
        txtTopDish.setText(itemMenu);
    }
}
