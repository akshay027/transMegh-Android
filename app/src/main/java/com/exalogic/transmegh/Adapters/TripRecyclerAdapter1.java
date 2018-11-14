package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exalogic.transmegh.Models.database.Trip;
import com.exalogic.transmegh.R;

import java.util.ArrayList;

/**
 * Created by Exalogic on 3/15/2017.
 */

public class TripRecyclerAdapter1 extends RecyclerView.Adapter<TripRecyclerAdapter1.ViewHolder> {

    private ArrayList<Trip> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private boolean isPickup;

    public TripRecyclerAdapter1(Activity activity, ArrayList<Trip> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trip, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Trip trip = arrayList.get(position);
        holder.tvStartTime.setText(trip.getStartTime());
        holder.tvEndTime.setText(trip.getEndTime());
        holder.tvTripName.setText(trip.getBusTripName());
        holder.tvbusnum.setText(trip.getBusno());
        holder.tvTripStopCount.setText("Stop count : " + trip.getBusStopCount());
        holder.ivActive.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.blink));
        if (trip.isTripActive()) {
            holder.llActive.setVisibility(View.VISIBLE);
        } else {
            holder.llActive.setVisibility(View.INVISIBLE);
        }
        if (trip.getTripstatus()==0)
        {
            holder.icomp.setVisibility(View.VISIBLE);
        }
        else {
            holder.icomp.setVisibility(View.GONE);
        }
// to be

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvStartTime, tvEndTime, tvTripName, tvTripStopCount, ivActive,tvbusnum,icomp;
        public RelativeLayout llActive;

        public ViewHolder(View v) {
            super(v);

            this.tvStartTime = (TextView) v.findViewById(R.id.tvStartTime);
            this.tvEndTime = (TextView) v.findViewById(R.id.tvEndTime);
            this.tvTripName = (TextView) v.findViewById(R.id.tvTripName);
            this.tvTripStopCount = (TextView) v.findViewById(R.id.tvTripStopCount);
            this.tvbusnum = (TextView) v.findViewById(R.id.tvbusnum);
            this.llActive = (RelativeLayout) v.findViewById(R.id.llActive);
            this.ivActive = (TextView) v.findViewById(R.id.ivActive);
            this.icomp = (TextView) v.findViewById(R.id.icomp);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<Trip> getCurrentDada() {
        return this.arrayList;
    }

}