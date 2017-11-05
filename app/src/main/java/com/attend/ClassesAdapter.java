package com.attend;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {

    private List<ClassList> classList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.nameOfClass);
            genre = (TextView) view.findViewById(R.id.location);
            year = (TextView) view.findViewById(R.id.time);
        }
    }


    public ClassesAdapter(List<ClassList> classList) {
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classses_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClassList list = classList.get(position);
        holder.title.setText(list.getNameOfClass());
        holder.genre.setText(list.getLocation());
        holder.year.setText(list.getTime());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}
