package eus.ilanda.eskatuetaordaindu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eus.ilanda.eskatuetaordaindu.R;

/**
 * Created by landa on 06/07/2018.
         */

public class FragmentBottomNav extends android.support.v4.app.Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_bottom_nav, container, false);
        return v;
    }
}