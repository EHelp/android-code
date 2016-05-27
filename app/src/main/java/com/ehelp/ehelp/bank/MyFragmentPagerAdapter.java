package com.ehelp.ehelp.bank;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by jeese on 2015/10/13.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragment_list;
    ArrayList<String> title_list;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragment_list, ArrayList<String> title_list) {
        super(fm);
        this.fragment_list = fragment_list;
        this.title_list = title_list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }
}
