package com.example.crm.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.crm.models.Location;
import com.example.crm.R;


import java.util.ArrayList;
import java.util.List;

public class AdapterListLocations extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Location> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Location obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListLocations(Context context, List<Location> items) {
        this.items = items;
        ctx = context;
    }
    public AdapterListLocations(List<Location> items) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView date;

        public View lyt_parent;
        public TextView locationName;
        public TextView accountName;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            locationName = (TextView) v.findViewById(R.id.locationName);
            accountName = (TextView) v.findViewById(R.id.leadName);
            date = (TextView) v.findViewById(R.id.date);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
//            website = (TextView) v.findViewById(R.id.website);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Location p = items.get(position);
            view.locationName.setText(p.name);
            view.date.setText(p.date);
//            view.address.setText("ID"+p.name);
            view.accountName.setText(p.account_name);
//            view.date.setText(p.phone);

            //Tools.displayImageRound(ctx, view.image, p.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

