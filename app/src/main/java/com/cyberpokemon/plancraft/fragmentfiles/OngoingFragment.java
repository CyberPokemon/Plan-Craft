package com.cyberpokemon.plancraft.fragmentfiles;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cyberpokemon.plancraft.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OngoingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton addTaskButton = view.findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialogBox();
            }
        });
    }

    private void showAddTaskDialogBox() {

        View dialogboxview = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogboxview);

        AlertDialog dialog = builder.create();

        Button submitButton = dialogboxview.findViewById(R.id.submitbutton);
        submitButton.setOnClickListener(v -> {
            dialog.dismiss(); // Just dismiss the dialog for now
        });

        dialog.show();
    }
}