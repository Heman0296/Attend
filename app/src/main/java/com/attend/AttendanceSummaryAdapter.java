package com.attend;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AttendanceSummaryAdapter extends RecyclerView.Adapter<AttendanceSummaryAdapter.MyViewHolder> {

    Context context;
    private List<AttendanceSummaryList> summaryList;

    public AttendanceSummaryAdapter(List<AttendanceSummaryList> summaryList, Context context) {
        this.summaryList = summaryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_summary_row, parent, false);

        return new MyViewHolder(itemView,context,summaryList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AttendanceSummaryList list = summaryList.get(position);
        holder.nameOfClass.setText(list.getNameOfClass());
        holder.percentage.setText(list.getPercentage());
    }

    public int getItemCount() {
        return summaryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameOfClass, percentage;
        Context context;
        List<AttendanceSummaryList> summaryList = new List<AttendanceSummaryList>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<AttendanceSummaryList> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(AttendanceSummaryList attendanceSummaryList) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends AttendanceSummaryList> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends AttendanceSummaryList> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public AttendanceSummaryList get(int index) {
                return null;
            }

            @Override
            public AttendanceSummaryList set(int index, AttendanceSummaryList element) {
                return null;
            }

            @Override
            public void add(int index, AttendanceSummaryList element) {

            }

            @Override
            public AttendanceSummaryList remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<AttendanceSummaryList> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<AttendanceSummaryList> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<AttendanceSummaryList> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        public MyViewHolder(View view, Context context, List<AttendanceSummaryList> summaryList) {
            super(view);
            this.summaryList = summaryList;
            this.context = context;
            view.setOnClickListener(this);
            nameOfClass = (TextView) view.findViewById(R.id.nameOfClass);
            percentage = (TextView) view.findViewById(R.id.percentage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            AttendanceSummaryList summaryObject = this.summaryList.get(position);
            Intent intent = new Intent(this.context, AttendanceSummaryDetail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("summaryObject", summaryObject);
            this.context.startActivity(intent);
        }
    }
}
