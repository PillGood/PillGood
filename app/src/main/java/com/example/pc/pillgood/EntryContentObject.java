package com.example.pc.pillgood;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class EntryContentObject implements Parent<String> {
    private String title;
    private String hospital;
    private ArrayList<String> options;
    private int icon;
    private long date;

    public EntryContentObject() {
        options = new ArrayList<>();
        date = 0L;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public List<String> getChildList() {
        return options;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
