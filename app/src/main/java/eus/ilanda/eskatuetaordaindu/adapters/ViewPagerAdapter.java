package eus.ilanda.eskatuetaordaindu.adapters;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> nFragmentList  = new ArrayList<>();
    private final List<String> nFragmentTitleList  = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        nFragmentList.add(fragment);
        nFragmentTitleList.add(title);
    }


    @Override
    public Fragment getItem(int position) {
        return nFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return nFragmentList.size();
    }
}
