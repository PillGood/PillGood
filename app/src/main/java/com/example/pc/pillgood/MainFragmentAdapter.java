package com.example.pc.pillgood;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String first;
    private String second;

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
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter
        switch (position) {
            case 0:
                first = createdFragment.getTag();
                break;
            case 1:
                second = createdFragment.getTag();
                break;
        }
        // ... save the tags somewhere so you can reference them later
        return createdFragment;
    }

    public ArrayList<String> getTabs(){
        ArrayList<String> tabs=new ArrayList<>();
        tabs.add(first);
        tabs.add(second);
        return tabs;
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