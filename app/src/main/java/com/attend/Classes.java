package com.attend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Classes extends Fragment {
    private List<ClassList> classList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClassesAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classes, container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ClassesAdapter(classList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
        return rootView;
    }

    private void prepareMovieData() {
        ClassList classObject = new ClassList("Mad Max: Fury Road", "Action & Adventure", "2015");
        classList.add(classObject);

        classObject = new ClassList("Inside Out", "Animation, Kids & Family", "2015");
        classList.add(classObject);

        classObject = new ClassList("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        classList.add(classObject);

        classObject = new ClassList("Shaun the Sheep", "Animation", "2015");
        classList.add(classObject);

        classObject = new ClassList("The Martian", "Science Fiction & Fantasy", "2015");
        classList.add(classObject);

        classObject = new ClassList("Mission: Impossible Rogue Nation", "Action", "2015");
        classList.add(classObject);

        classObject = new ClassList("Up", "Animation", "2009");
        classList.add(classObject);

        classObject = new ClassList("Star Trek", "Science Fiction", "2009");
        classList.add(classObject);

        classObject = new ClassList("The LEGO Movie", "Animation", "2014");
        classList.add(classObject);

        classObject = new ClassList("Iron Man", "Action & Adventure", "2008");
        classList.add(classObject);

        classObject = new ClassList("Aliens", "Science Fiction", "1986");
        classList.add(classObject);

        classObject = new ClassList("Chicken Run", "Animation", "2000");
        classList.add(classObject);

        classObject = new ClassList("Back to the Future", "Science Fiction", "1985");
        classList.add(classObject);

        classObject = new ClassList("Raiders of the Lost Ark", "Action & Adventure", "1981");
        classList.add(classObject);

        classObject = new ClassList("Goldfinger", "Action & Adventure", "1965");
        classList.add(classObject);

        classObject = new ClassList("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        classList.add(classObject);

        mAdapter.notifyDataSetChanged();
    }
}
