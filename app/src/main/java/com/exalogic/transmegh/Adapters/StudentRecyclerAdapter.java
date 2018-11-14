package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exalogic.transmegh.Activities.CircleTransform;
import com.exalogic.transmegh.Database.PreferencesManger;
import com.exalogic.transmegh.Models.database.Student;
import com.exalogic.transmegh.R;
import com.exalogic.transmegh.Utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Nikhil on 12-11-2015.
 */
public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolder> {

    private ArrayList<Student> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private boolean isTripRunning, isDrop, isActiveTrip;
    private Context context;

    public StudentRecyclerAdapter(Context context, Activity activity, ArrayList<Student> arrayList, boolean isTripRunning, boolean isDrop) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.isTripRunning = isTripRunning;
        this.isDrop = isDrop;
        this.context = context;
        this.isActiveTrip = PreferencesManger.getBooleanFields(activity, Constants.Pref.KEY_IS_CURRENT);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Student student = arrayList.get(position);

        holder.tvStudentName.setText(student.getName());
//        boolean b = isTripRunning ? (isDrop ? false : true) : (isDrop) ? true : false;

            if (isTripRunning) {
                holder.llAttendance.setVisibility(View.VISIBLE);
                holder.checkIn.setVisibility((isDrop ? true : isTripRunning) ? View.VISIBLE : View.INVISIBLE);
                holder.checkOut.setVisibility((isDrop && isTripRunning) ? View.VISIBLE : View.INVISIBLE);
                holder.checkIn.setChecked(student.isCheckIn());
                holder.checkOut.setChecked(student.isCheckOut());

            } else {
                holder.llAttendance.setVisibility(View.GONE);

        }
        Picasso.with(context).load(student.getPhoto()).resize(50, 50)
                .transform(new CircleTransform()).into(holder.tvLogo);
//        if (isTripRunning) {
//            if (isActiveTrip) {
//                if ((isTripRunning || isDrop)) {
//                    holder.llAttendance.setVisibility(View.VISIBLE);
//                    if ((isDrop && !isTripRunning) || (!isDrop && isTripRunning)) {
//                        holder.checkIn.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.checkIn.setVisibility(View.INVISIBLE);
//                    }
//                    if (isDrop && isTripRunning) {
//                        holder.checkIn.setVisibility(View.VISIBLE);
//                        holder.checkOut.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.checkOut.setVisibility(View.INVISIBLE);
//                    }
//                    holder.checkIn.setChecked(student.isCheckIn());
//                    holder.checkOut.setChecked(student.isCheckOut());
//                } else {
//                    holder.llAttendance.setVisibility(View.GONE);
//                }
//            } else {
//                holder.llAttendance.setVisibility(View.GONE);
//            }
//        } else {
//            if ((isTripRunning || isDrop)) {
//                holder.llAttendance.setVisibility(View.VISIBLE);
//                if ((isDrop && !isTripRunning) || (!isDrop && isTripRunning)) {
//                    holder.checkIn.setVisibility(View.VISIBLE);
//                } else {
//                    holder.checkIn.setVisibility(View.INVISIBLE);
//                }
//                if (isDrop && isTripRunning) {
//                    holder.checkOut.setVisibility(View.VISIBLE);
//                } else {
//                    holder.checkOut.setVisibility(View.INVISIBLE);
//                }
//                holder.checkIn.setChecked(student.isCheckIn());
//                holder.checkOut.setChecked(student.isCheckOut());
//            } else {
//                holder.llAttendance.setVisibility(View.GONE);
//            }
//        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvStudentName;
        LinearLayout llbg;
        ImageView ivMessage, ivCall, tvLogo;

        LinearLayout llAttendance;
        CheckBox checkIn, checkOut;

        public ViewHolder(View v) {
            super(v);

            this.llbg = (LinearLayout) v.findViewById(R.id.llbg);
            this.llAttendance = (LinearLayout) v.findViewById(R.id.llAttendance);
            this.tvStudentName = (TextView) v.findViewById(R.id.tvStudentName);
            this.ivMessage = (ImageView) v.findViewById(R.id.ivMessage);
            this.ivCall = (ImageView) v.findViewById(R.id.ivCall);
            this.tvLogo = (ImageView) v.findViewById(R.id.tvLogo);
            this.checkIn = (CheckBox) v.findViewById(R.id.checkIn);
            this.checkOut = (CheckBox) v.findViewById(R.id.checkOut);

            this.ivCall.setOnClickListener(this);
            this.ivMessage.setOnClickListener(this);
            v.setOnClickListener(this);
            this.checkOut.setOnClickListener(this);
            this.checkIn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v.getId() == R.id.ivCall) {
                    mItemClickListener.onCall(v, getAdapterPosition());
                } else if (v.getId() == R.id.ivMessage) {
                    mItemClickListener.onMessage(v, getAdapterPosition());
                } else if (v.getId() == R.id.checkIn) {
                    mItemClickListener.onCheckIn(v, getAdapterPosition());
                } else if (v.getId() == R.id.checkOut) {
                    mItemClickListener.onCheckOut(v, getAdapterPosition());
                } else {
                    mItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

        public void onMessage(View view, int position);

        public void onCall(View view, int position);

        public void onCheckIn(View view, int position);

        public void onCheckOut(View view, int position);

    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<Student> getCurrentDada() {
        return this.arrayList;
    }

}