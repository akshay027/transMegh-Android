package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exalogic.transmegh.Models.Route;
import com.exalogic.transmegh.R;

import java.util.ArrayList;


/**
 * Created by Nikhil on 24-11-2015.
 */
public class StudentStopRecyclerAdapter extends RecyclerView.Adapter<StudentStopRecyclerAdapter.ViewHolder> {

    private ArrayList<Route> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private boolean isAttendance;

    public StudentStopRecyclerAdapter(Activity activity, ArrayList<Route> arrayList, boolean isAttendance) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.isAttendance = isAttendance;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_route, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Route eveningShift = arrayList.get(position);

        holder.tvName.setText("" + eveningShift.getStopName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvName;
        public ImageView ivNav;

        public ViewHolder(View v) {
            super(v);

            this.tvName = (TextView) v.findViewById(R.id.tvName);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ivNav:
                    mItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                default:
                    mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);


    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<Route> getCurrentDada() {
        return this.arrayList;
    }

}