package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exalogic.transmegh.Models.database.BusStop;
import com.exalogic.transmegh.R;

import java.util.ArrayList;


/**
 * Created by Nikhil on 24-11-2015.
 */
public class MorningShiftRecyclerAdapter extends RecyclerView.Adapter<MorningShiftRecyclerAdapter.ViewHolder> {

    private ArrayList<BusStop> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private boolean isMorning;

    public MorningShiftRecyclerAdapter(Activity activity, ArrayList<BusStop> arrayList) {
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

        BusStop BusStop = arrayList.get(position);

        holder.tvName.setText("" + BusStop.getStopName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvName, tvTime;
        public ImageView ivNav;

        public ViewHolder(View v) {
            super(v);

            this.tvName = (TextView) v.findViewById(R.id.tvName);
            this.tvTime = (TextView) v.findViewById(R.id.tvTime);

            this.ivNav = (ImageView) v.findViewById(R.id.ivNav);
            this.ivNav.setOnClickListener(this);

            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ivNav:
                    mItemClickListener.onLocationClick(v, getAdapterPosition());
                    break;
                default:
                    mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

        public void onLocationClick(View view, int position);

    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<BusStop> getCurrentDada() {
        return this.arrayList;
    }

}