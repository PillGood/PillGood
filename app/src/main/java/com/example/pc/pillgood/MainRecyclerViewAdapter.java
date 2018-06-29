package com.example.pc.pillgood;

import android.content.Context;
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

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends ExpandableRecyclerAdapter<EntryContentObject, String, MainRecyclerViewAdapter.EntryContentHolder, MainRecyclerViewAdapter.OptionEntryHolder> {
    public ArrayList<EntryContentObject> entries = new ArrayList<>();
    private Context context;
    private final int TYPE_CHILD = 10;
    private final int TYPE_PARENT_1 = 135;
    private final int TYPE_PARENT_2 = 234;
    private int type;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapter(Context context, ArrayList<EntryContentObject> entries, int type) {
        super(entries);
        this.context = context;
        this.entries.addAll(entries);
        this.type=type;
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
        if(type==0) {
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
            if(type==TYPE_PARENT_1){
                doneButton=convertView.findViewById(R.id.doneButton);
            }
        }
        private void bind(final int groupPosition) {
            final EntryContentObject entryContentObject = entries.get(groupPosition);
            tvTitle.setText(entryContentObject.getTitle());
            tvDate.setText(entryContentObject.getDate()+"");
            tvHospital.setText(entryContentObject.getHospital());
            if(type==TYPE_PARENT_1) {
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            expandButton =  itemView.findViewById(R.id.down);
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
            tvTitle.setText(entries.get(parentPosition).getChildList().get(childPosition));
        }

    }
}
