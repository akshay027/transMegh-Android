package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exalogic.transmegh.Activities.CircleTransform;
import com.exalogic.transmegh.Models.Parent;
import com.exalogic.transmegh.R;
import com.exalogic.transmegh.Utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Nikhil on 24-11-2015.
 */
public class ParentListRecyclerAdapter extends RecyclerView.Adapter<ParentListRecyclerAdapter.ViewHolder> {

    private ArrayList<Parent> arrayList;
    private Activity activity;
    private boolean isAmPm, isHoliday;
    static OnItemClickListener mItemClickListener;
    private Context context;


    public ParentListRecyclerAdapter(Context context, Activity activity, ArrayList<Parent> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parent, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Parent chatDriver = arrayList.get(position);

        holder.tvStudentName.setText(chatDriver.getName());
        holder.tvParentName.setText(chatDriver.getFatherName());
        if (TextUtils.isEmpty(chatDriver.getFatherName())) {
            holder.tvParentName.setText(chatDriver.getMotherName());
        }
        else if (TextUtils.isEmpty(chatDriver.getMotherName())||TextUtils.isEmpty(chatDriver.getFatherName())) {
            holder.tvParentName.setText(chatDriver.getGuardianName());
        }
        Picasso.with(context).load(chatDriver.getPhoto()).resize(50, 50)
                .transform(new CircleTransform()).into(holder.tvLogo);



/*

        switch (position % 5) {
            case 0:
                holder.tvLogo.setBackgroundColor(activity.getResources().getColor(R.color.color_6));
                break;
            case 1:
                holder.tvLogo.setBackgroundColor(activity.getResources().getColor(R.color.color_2));
                break;
            case 2:
                holder.tvLogo.setBackgroundColor(activity.getResources().getColor(R.color.color_3));
                break;
            case 3:
                holder.tvLogo.setBackgroundColor(activity.getResources().getColor(R.color.color_4));
                break;
            case 4:
                holder.tvLogo.setBackgroundColor(activity.getResources().getColor(R.color.color_5));
                break;
        }

*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvStudentName, tvParentName;
        ImageView tvLogo;
        public LinearLayout llMessage;
        private ImageView ivMessage, ivCall, ivChat;

        public ViewHolder(View v) {
            super(v);

            this.tvStudentName = (TextView) v.findViewById(R.id.tvStudentName);
            this.tvParentName = (TextView) v.findViewById(R.id.tvParentName);
            this.tvLogo = (ImageView) v.findViewById(R.id.tvLogo);
            this.ivCall = (ImageView) v.findViewById(R.id.ivCall);
            this.ivMessage = (ImageView) v.findViewById(R.id.ivMessage);
            this.ivChat = (ImageView) v.findViewById(R.id.ivChat);

            v.setOnClickListener(this);
            this.ivCall.setOnClickListener(this);
            this.ivMessage.setOnClickListener(this);
            this.ivChat.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                if (v.getId() == R.id.ivCall) {
                    mItemClickListener.onClickCall(v, getAdapterPosition());
                } else if (v.getId() == R.id.ivMessage) {
                    mItemClickListener.onClickMessage(v, getAdapterPosition());
                } else {
                    mItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }


    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

        public void onClickCall(View view, int position);

        public void onClickMessage(View view, int position);

        public void onClickChat(View view, int position);

    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}