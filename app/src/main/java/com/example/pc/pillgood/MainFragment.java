package com.example.pc.pillgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView rvContent;
    private MainRecyclerViewAdapter adapter;
    private ArrayList<EntryContentObject> entryContentObjects =new ArrayList<>();
    private int type;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
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
        entryContentObjects.clear();
        EntryDatabaseHandler entryDatabaseHandler=EntryDatabaseHandler.getInstance(getContext());
        if(getType()==0){
            List<Entry> entries=entryDatabaseHandler.getUndoneEntries();
            for(Entry entry:entries) {
                EntryContentObject entryContentObject = new EntryContentObject();
                entryContentObject.setTitle(entry.getTitle());
                entryContentObject.setHospital(entry.getHospital());
                entryContentObject.setDate(entry.getDate());
                entryContentObject.setOptions(entry.getSubEntries());
                entryContentObject.setDone(entry.getDone());
                entryContentObjects.add(entryContentObject);
            }
            adapter=new MainRecyclerViewAdapter(getContext(), entryContentObjects, getType());
        }else{
            List<Entry> entries=entryDatabaseHandler.getDoneEntries();
            for(Entry entry:entries) {
                EntryContentObject entryContentObject = new EntryContentObject();
                entryContentObject.setTitle(entry.getTitle());
                entryContentObject.setHospital(entry.getHospital());
                entryContentObject.setDate(entry.getDate());
                entryContentObject.setOptions(entry.getSubEntries());
                entryContentObject.setDone(entry.getDone());
                entryContentObjects.add(entryContentObject);
            }
            adapter=new MainRecyclerViewAdapter(getContext(), entryContentObjects, getType());
        }

        rvContent.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvContent.setLayoutManager(layoutManager);
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    public void notifyAdapter(){
        EntryDatabaseHandler entryDatabaseHandler=EntryDatabaseHandler.getInstance(getContext());
        entryContentObjects.clear();
        if(getType()==0){
            List<Entry> entries=entryDatabaseHandler.getUndoneEntries();
            for(Entry entry:entries) {
                EntryContentObject entryContentObject = new EntryContentObject();
                entryContentObject.setTitle(entry.getTitle());
                entryContentObject.setHospital(entry.getHospital());
                entryContentObject.setDate(entry.getDate());
                entryContentObject.setOptions(entry.getSubEntries());
                entryContentObject.setDone(entry.getDone());
                entryContentObjects.add(entryContentObject);
            }
        }else{
            List<Entry> entries=entryDatabaseHandler.getDoneEntries();
            for(Entry entry:entries) {
                EntryContentObject entryContentObject = new EntryContentObject();
                entryContentObject.setTitle(entry.getTitle());
                entryContentObject.setHospital(entry.getHospital());
                entryContentObject.setDate(entry.getDate());
                entryContentObject.setOptions(entry.getSubEntries());
                entryContentObject.setDone(entry.getDone());
                entryContentObjects.add(entryContentObject);
            }
        }
//        adapter.swap(entryContentObjects);
//        adapter.setEntryObjects(entryContentObjects);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        rvContent.setLayoutManager(layoutManager);
//        rvContent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyParentRangeChanged(0, adapter.getItemCount());
    }
}
