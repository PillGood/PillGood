package com.example.pc.pillgood;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pc on 2017-06-30.
 */

public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String titles[];
    private int icons[];
    private OnItemSelectedListener mListener;

    public NavigationAdapter(String Titles[], int Icons[]) {
        titles = Titles;
        icons = Icons;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_header, parent, false);
                return new HeaderViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_element, parent, false);
                return new ElementViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                break;
            case 1:
                ElementViewHolder elementViewHolder = (ElementViewHolder) holder;
                elementViewHolder.tvTitle.setText(titles[position - 1]);
                elementViewHolder.ivIcon.setImageResource(icons[position - 1]);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        return 1;
    }
    public void setOnItemClickListener(OnItemSelectedListener mListener) {
        this.mListener = mListener;
    }
    public interface OnItemSelectedListener {
        void onItemSelected(View v, int position);
    }

    public class ElementViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        TextView tvTitle;
        ImageView ivIcon;

        public ElementViewHolder(View itemView) {
            super(itemView);
            tvTitle =  itemView.findViewById(R.id.rowText);
            ivIcon =  itemView.findViewById(R.id.rowIcon);
            holderId = 1;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            holderId = 0;
        }
    }
}