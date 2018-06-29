package com.example.pc.pillgood;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends ExpandableRecyclerAdapter<EntryContentObject, String, MainRecyclerViewAdapter.EntryContentHolder, MainRecyclerViewAdapter.OptionEntryHolder> {
    public ArrayList<EntryContentObject> entryObjects = new ArrayList<>();
    public ArrayList<Entry> entries=new ArrayList<>();
    private Context context;
    private final int TYPE_CHILD = 10;
    private final int TYPE_PARENT_1 = 0;
    private final int TYPE_PARENT_2 = 1;
    private int type;
    public EntryDatabaseHandler db;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapter(Context context, ArrayList<EntryContentObject> entries, int type) {
        super(entries);
        this.context = context;
        entryObjects.clear();
        entryObjects.addAll(entries);
        this.type=type;
        db=EntryDatabaseHandler.getInstance(context);
    }

    @NonNull
    @Override
    public EntryContentHolder onCreateParentViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(viewType==TYPE_PARENT_1) {
            View convertView = layoutInflater.inflate(R.layout.item_entry_parent_1, parent, false);
            return new EntryContentHolder(convertView, viewType);
        }else{
            View convertView = layoutInflater.inflate(R.layout.item_entry_parent_2, parent, false);
            return new EntryContentHolder(convertView, viewType);
        }
    }

    @NonNull
    @Override
    public OptionEntryHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View convertView = layoutInflater.inflate(R.layout.item_entry_child, parent, false);
        return new OptionEntryHolder(convertView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull EntryContentHolder parentViewHolder, int parentPosition, @NonNull EntryContentObject parent) {
        parentViewHolder.bind(parentPosition);
    }

    @Override
    public void onBindChildViewHolder(@NonNull OptionEntryHolder childViewHolder, int parentPosition, int childPosition, @NonNull String child) {
        childViewHolder.bind(parentPosition, childPosition);
    }

    @Override
    public int getParentViewType(int parentPosition) {
        if(type==0){
            return TYPE_PARENT_1;
        }else{
            return TYPE_PARENT_2;
        }
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        return TYPE_CHILD;
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == TYPE_PARENT_1||viewType==TYPE_PARENT_2;
    }

    public void swap(ArrayList<EntryContentObject> list){
        if (entryObjects != null) {
            entryObjects.clear();
            entryObjects.addAll(list);
        }
        else {
            entryObjects = list;
        }
        notifyDataSetChanged();
    }

    public class EntryContentHolder extends ParentViewHolder {
        // each data item is just a string in this case
        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvHospital;
        public int type;
        public Button doneButton;
        private ImageView expandButton;

        public EntryContentHolder(View convertView, int type) {
            super(convertView);
            this.type=type;
            tvTitle=convertView.findViewById(R.id.name);
            tvDate=convertView.findViewById(R.id.date);
            tvHospital=convertView.findViewById(R.id.hospital);
            expandButton = convertView.findViewById(R.id.down);
            if(type==TYPE_PARENT_1){
                doneButton=convertView.findViewById(R.id.doneButton);
            }else{
                tvTitle.setPaintFlags(tvTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        private void bind(final int groupPosition) {
            if(entryObjects.size()>0) {
                final EntryContentObject entryContentObject = entryObjects.get(groupPosition);
                tvTitle.setText(entryContentObject.getTitle());
                DateTime dateTime=new DateTime(entryContentObject.getDate());
                DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy.MM.dd");
                tvDate.setText(dtfOut.print(dateTime));
                tvHospital.setText(entryContentObject.getHospital());
                if (type == TYPE_PARENT_1) {
                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Entry entry = db.getUndoneEntries().get(groupPosition);
                            entry.setDone(1);
                            db.updateEntry(entry);
                            notifyParentRemoved(groupPosition);
                        }
                    });
                }
                expandButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded()) {
                            collapseView();
                        } else {
                            expandView();
                        }
                    }
                });
            }
        }
    }

    public class OptionEntryHolder extends ChildViewHolder {
        private TextView tvTitle;

        public OptionEntryHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        private void bind(int parentPosition, int childPosition) {
            tvTitle.setText(entryObjects.get(parentPosition).getChildList().get(childPosition));
        }

    }
}
