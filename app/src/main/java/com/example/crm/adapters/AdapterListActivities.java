package com.example.crm.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.crm.models.Account;
import com.example.crm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterListActivities extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, String>> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Map<String, String> obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListActivities(Context context, List<Map<String, String>> items) {
        this.items = items;
        ctx = context;
    }
    public AdapterListActivities(List<Map<String, String>> items) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
//        public TextView name;
//        public TextView date;
//        public TextView value;
//        public TextView address;
        public View lyt_parent;
        public TextView accountName;
        public TextView contact;
        public TextView time;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            accountName = (TextView) v.findViewById(R.id.accountName);
            contact = (TextView) v.findViewById(R.id.contact);
            time = (TextView) v.findViewById(R.id.time);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;


            Map<String,String> p = items.get(position);
            view.accountName.setText(p.get("accountName"));
            view.contact.setText(p.get("type")+" : "+p.get("contact"));
            view.time.setText(p.get("time"));

            if(p.get("type").equals("Call")){
                view.image.setImageResource(R.drawable.ic_call);
            }else if(p.get("type").equals("Meeting")){
                view.image.setImageResource(R.drawable.ic_peoples);
            }else if(p.get("type").equals("Task")){
                view.image.setImageResource(R.drawable.ic_description);
            }else if(p.get("type").equals("Visit")){
                view.image.setImageResource(R.drawable.ic_business);
            }

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
