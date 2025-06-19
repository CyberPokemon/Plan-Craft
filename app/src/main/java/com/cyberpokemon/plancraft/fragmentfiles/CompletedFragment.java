package com.cyberpokemon.plancraft.fragmentfiles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyberpokemon.plancraft.DatabaseHelper;
import com.cyberpokemon.plancraft.R;
import com.cyberpokemon.plancraft.Task;
import com.cyberpokemon.plancraft.adapters.TaskAdapter;
import com.cyberpokemon.plancraft.adapters.TaskCompletedAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompletedFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Task> taskList = new ArrayList<>();
    TaskCompletedAdapter taskCompletedAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.completeTaskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTaskFromDatabase();
    }

    private void loadTaskFromDatabase() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        taskList = db.getAllCompleteTasks();

        taskCompletedAdapter= new TaskCompletedAdapter(taskList,getContext(),this::loadTaskFromDatabase);
        recyclerView.setAdapter(taskCompletedAdapter);
    }
}