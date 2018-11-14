package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.R;

import java.util.ArrayList;


/**
 * Created by Nikhil on 12-11-2015.
 */
public class BusStopRecyclerAdapter extends RecyclerView.Adapter<BusStopRecyclerAdapter.ViewHolder> {

    private ArrayList<BusStop> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private boolean isPickup;

    public BusStopRecyclerAdapter(Activity activity, ArrayList<BusStop> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_stop, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        BusStop busStop = arrayList.get(position);
        holder.tvTime.setText(busStop.getTiming());
        holder.tvStopName.setText(busStop.getStopName());
        holder.tvAddress.setText(busStop.getAddress());
        holder.tvCount.setText(busStop.getStudentCount());



/*
        switch (position % 5) {
            case 0:
                holder.llbg.setBackgroundColor(activity.getResources().getColor(R.color.color_1));
                break;
            case 1:
                holder.llbg.setBackgroundColor(activity.getResources().getColor(R.color.color_2));
                break;
            case 2:
                holder.llbg.setBackgroundColor(activity.getResources().getColor(R.color.color_3));
                break;
            case 3:
                holder.llbg.setBackgroundColor(activity.getResources().getColor(R.color.color_4));
                break;
            case 4:
                holder.llbg.setBackgroundColor(activity.getResources().getColor(R.color.color_5));
                break;
        }*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvTime, tvStopName, tvAddress,tvCount;
        LinearLayout llbg;
        ImageView ivLocation;

        public ViewHolder(View v) {
            super(v);

            this.llbg = (LinearLayout) v.findViewById(R.id.llbg);
            this.tvTime = (TextView) v.findViewById(R.id.tvTime);
            this.tvStopName = (TextView) v.findViewById(R.id.tvStopName);
            this.tvAddress = (TextView) v.findViewById(R.id.tvAddress);
            this.tvCount=(TextView)v.findViewById(R.id.tvCount);

            this.ivLocation = (ImageView) v.findViewById(R.id.ivLocation);
            this.ivLocation.setOnClickListener(this);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v.getId() == R.id.ivLocation) {
                    mItemClickListener.onLocation(v, getAdapterPosition());
                } else {
                    mItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

        public void onLocation(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<BusStop> getCurrentDada() {
        return this.arrayList;
    }

}