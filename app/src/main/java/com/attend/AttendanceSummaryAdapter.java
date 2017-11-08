package com.attend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AttendanceSummaryAdapter extends RecyclerView.Adapter<AttendanceSummaryAdapter.MyViewHolder> {

    private List<AttendanceSummaryList> summaryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameOfClass, percentage;

        public MyViewHolder(View view) {
            super(view);
            nameOfClass = (TextView) view.findViewById(R.id.nameOfClass);
            percentage = (TextView) view.findViewById(R.id.percentage);
        }
    }


    public AttendanceSummaryAdapter(List<AttendanceSummaryList> summaryList) {
        this.summaryList = summaryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_summary_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AttendanceSummaryList list = summaryList.get(position);
        holder.nameOfClass.setText(list.getNameOfClass());
        holder.percentage.setText(list.getPercentage());
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }
}
