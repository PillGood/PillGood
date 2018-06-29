package com.example.pc.pillgood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by pc on 2017-06-20.
 */

public class Entry implements Serializable {
    public static Comparator<Entry> entryComparator = new Comparator<Entry>() {
        public int compare(Entry s1, Entry s2) {
            long count1 = s1.getDate();
            long count2 = s2.getDate();
            if (count1 > count2) {
                return 1;
            } else {
                return -1;
            }
        }
    };
    private int id;
    private String title;
    private String hospital;
    private Long date;
    private ArrayList<String> subEntries=new ArrayList<>();
    private int done=0;

    public Entry() {
        id = 0;
        title = "";
        hospital = "";
        date = 0L;
    }

    public Entry(int i, String s, String h, Long d) {
        id = i;
        title = s;
        hospital = h;
        date = d;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String n) {
        title = n;
    }

    public String getHospital() {
        return hospital;
    }

    public Long getDate() {
        return date;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setDate(Long date) {
        this.date = date;
    }
    public void makeDone(){
        done=1;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getDone() {
        return done;
    }

    public ArrayList<String> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(ArrayList<String> subEntries) {
        this.subEntries = subEntries;
    }

    //    public ArrayList<Object> getAllData() {
//        ArrayList<Object> result = new ArrayList<>();
//        result.add(getId());
//        result.add(getType());
//        result.add(getTitle());
//        result.add(getAmount());
//        result.add(getCategory());
//        result.add(getWhen());
//        result.add(getNote());
//        result.add(getPayment());
//        result.add(getCurrency());
//        result.add(getOriginalCurrency());
//        result.add(getOriginalAmount());
//        return result;
//    }

    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Entry) {
            if (((Entry) other).title.equals(this.title) &&
                    ((Entry) other).hospital == this.hospital &&
                    ((Entry) other).date.equals(this.date)) {
                result = true;
            }
        }
        return result;
    }
}
