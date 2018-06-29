package com.example.pc.pillgood;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        MainFragment fragment=new MainFragment();
        fragment.setType(position);
        return fragment;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public String getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.current_fragment);
            case 1:
                return mContext.getString(R.string.past_fragment);
            default:
                return null;
        }
    }

}