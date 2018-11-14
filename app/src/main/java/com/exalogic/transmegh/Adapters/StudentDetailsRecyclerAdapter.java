//package com.exalogic.inmegh.driver.Adapters;
//
//import android.app.Activity;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.exalogic.inmegh.driver.Models.database.Student;
//import com.exalogic.inmegh.driver.Models.database.Student;
//import com.exalogic.inmegh.driver.R;
//
//import java.util.ArrayList;
//
//
///**
// * Created by Nikhil on 24-11-2015.
// */
//public class StudentDetailsRecyclerAdapter extends RecyclerView.Adapter<StudentDetailsRecyclerAdapter.ViewHolder> {
//
//    private ArrayList<Student> arrayList;
//    private Activity activity;
//    public static OnItemClickListener mItemClickListener;
//    private boolean isMorning;
//
//    public StudentDetailsRecyclerAdapter(Activity activity, ArrayList<Student> arrayList) {
//        this.arrayList = arrayList;
//        this.activity = activity;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
//        ViewHolder vh = new ViewHolder(v);
//
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//
//        Student student = arrayList.get(position);
//
//        holder.tvName.setText("" + student.getName());
//        if (TextUtils.isEmpty(student.getParentName())) {
//            holder.tvParentName.setVisibility(View.GONE);
//        } else {
//            holder.tvParentName.setText("" + student.getParentName());
//            holder.tvParentName.setVisibility(View.GONE);
//        }
//
//        holder.tvContactNumber.setText("" + student.getContactNo());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        // each data item is just a string in this case
//        public TextView tvName, tvContactNumber, tvParentName;
//        public ImageView ivCall, ivMessage;
//
//        public ViewHolder(View v) {
//            super(v);
//
//            this.tvName = (TextView) v.findViewById(R.id.tvName);
//            this.tvContactNumber = (TextView) v.findViewById(R.id.tvContactNumber);
//            this.tvParentName = (TextView) v.findViewById(R.id.tvParentName);
//
//            this.ivCall = (ImageView) v.findViewById(R.id.ivCall);
//            this.ivMessage = (ImageView) v.findViewById(R.id.ivMessage);
//
//            this.ivMessage.setOnClickListener(this);
//            this.ivCall.setOnClickListener(this);
//
//            v.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()) {
//                case R.id.ivCall:
//                    mItemClickListener.onCallClick(v, getAdapterPosition());
//                    break;
//                case R.id.ivMessage:
//                    mItemClickListener.onMessageClick(v, getAdapterPosition());
//                    break;
//                default:
//                    mItemClickListener.onItemClick(v, getAdapterPosition());
//            }
//        }
//    }
//
//    public interface OnItemClickListener {
//
//        public void onItemClick(View view, int position);
//
//        public void onCallClick(View view, int position);
//
//        public void onMessageClick(View view, int position);
//
//
//    }
//
//    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
//        this.mItemClickListener = mItemClickListener;
//    }
//
//
//    public ArrayList<Student> getCurrentDada() {
//        return this.arrayList;
//    }
//
//}