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

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {

    Context context;
    private List<ClassList> classList;

    public ClassesAdapter(List<ClassList> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classses_list_row, parent, false);

        return new MyViewHolder(itemView,context,classList);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, year, genre;
        Context context;
        List<ClassList> classlist = new List<ClassList>() {
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
            public Iterator<ClassList> iterator() {
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
            public boolean add(ClassList classList) {
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
            public boolean addAll(@NonNull Collection<? extends ClassList> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends ClassList> c) {
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
            public ClassList get(int index) {
                return null;
            }

            @Override
            public ClassList set(int index, ClassList element) {
                return null;
            }

            @Override
            public void add(int index, ClassList element) {

            }

            @Override
            public ClassList remove(int index) {
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
            public ListIterator<ClassList> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<ClassList> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<ClassList> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        public MyViewHolder(View view, Context context, List<ClassList> classList) {
            super(view);
            this.classlist = classList;
            this.context = context;
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.nameOfClass);
            genre = (TextView) view.findViewById(R.id.location);
            year = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ClassList classObject = this.classlist.get(position);
            Intent intent = new Intent(this.context, RangingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("classObject", classObject);
            this.context.startActivity(intent);
        }
    }
}
