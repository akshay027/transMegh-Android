package com.exalogic.transmegh.Adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exalogic.transmegh.Models.database.Chat;
import com.exalogic.transmegh.Utility.Constants;
import com.exalogic.transmegh.R;

import java.util.ArrayList;


/**
 * Created by Nikhil on 24-11-2015.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

    private ArrayList<Chat> arrayList;
    private Activity activity;

    public ChatRecyclerAdapter(Activity activity, ArrayList<Chat> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Chat chat = arrayList.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        if (chat.getType() == 1) { //receiver

            params.weight = 1.0f;
            params.gravity = Gravity.LEFT;

            params.setMargins(convertDpToPixel(10), convertDpToPixel(5), convertDpToPixel(60), convertDpToPixel(10));
            holder.llMessage.setLayoutParams(params);
            holder.tvTime.setLayoutParams(params);

            holder.tvMessage.setBackgroundColor(activity.getResources().getColor(R.color.receiver));
            holder.tvTime.setBackgroundColor(activity.getResources().getColor(R.color.receiver));

        } else { // sender
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;

            params.setMargins(convertDpToPixel(60), convertDpToPixel(5), convertDpToPixel(10), convertDpToPixel(10));
            holder.llMessage.setLayoutParams(params);
            holder.tvTime.setLayoutParams(params);

            holder.tvMessage.setBackgroundColor(activity.getResources().getColor(R.color.sender));
            holder.tvTime.setBackgroundColor(activity.getResources().getColor(R.color.sender));
        }

        holder.tvMessage.setText(chat.getMessage());
        if (chat.getStatus() == Constants.SENT) {
            holder.tvTime.setText(chat.getTime() + " Sent");
        } else {
            holder.tvTime.setText(chat.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvMessage, tvTime;
        public LinearLayout llMessage;

        public ViewHolder(View v) {
            super(v);

            this.tvMessage = (TextView) v.findViewById(R.id.tvMessage);
            this.tvTime = (TextView) v.findViewById(R.id.tvTime);
            this.llMessage = (LinearLayout) v.findViewById(R.id.llMessage);
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

//            switch (v.getId()) {
//                case R.id.tvAM:
//                    mItemClickListener.onAMClick(v, getAdapterPosition());
//                    break;
//                case R.id.tvPM:
//                    mItemClickListener.onPMClick(v, getAdapterPosition());
//                    break;
//                case R.id.viewPresentAbsent:
//                    mItemClickListener.onPresentAbsentClick(v, getAdapterPosition());
//                    break;
//                default:
//                    mItemClickListener.onItemClick(v, getAdapterPosition());
//            }
        }
    }


    public int convertDpToPixel(float dp) {
        Resources resources = activity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }


}