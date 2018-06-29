package com.example.pc.pillgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private RecyclerView rvContent;
    private MainRecyclerViewAdapter adapter;
    private ArrayList<EntryContentObject> entryContentObjects =new ArrayList<>();
    private int type;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);
        rvContent=rootView.findViewById(R.id.content);
        for(int i=0; i<3; i++) {
            EntryContentObject entryContentObject = new EntryContentObject();
            entryContentObject.setTitle("name");
            entryContentObject.setHospital("hospital");
            entryContentObject.setDate(234235324);
            ArrayList<String> temp = new ArrayList<>();
            temp.add("sssssss");
            temp.add("fffffff");
            entryContentObject.setOptions(temp);
            entryContentObjects.add(entryContentObject);
        }
        adapter=new MainRecyclerViewAdapter(getContext(), entryContentObjects, getType());
        rvContent.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvContent.setLayoutManager(layoutManager);
        return rootView;
    }

}
