package com.cyberpokemon.plancraft.fragmentfiles;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cyberpokemon.plancraft.DatabaseHelper;
import com.cyberpokemon.plancraft.R;
import com.cyberpokemon.plancraft.Task;
import com.cyberpokemon.plancraft.adapters.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OngoingFragment extends Fragment {
    private TextView deadlineTextView;
    private TextView updateDeadlineTextView;

    private Calendar selectedDeadline = Calendar.getInstance();
    private RecyclerView recyclerView;
    private List<Task> taskList = new ArrayList<>();

    TaskAdapter taskAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTaskFromDatabase();

        FloatingActionButton addTaskButton = view.findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialogBox();
            }
        });
    }

    private void loadTaskFromDatabase() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        taskList = db.getAllIncompleteTasks();

//        taskAdapter= new TaskAdapter(taskList,getContext(),this::loadTaskFromDatabase);
        taskAdapter = new TaskAdapter(taskList,getContext(),this::loadTaskFromDatabase,this::showEditTaskDialogBox);
        recyclerView.setAdapter(taskAdapter);
    }

    private void showEditTaskDialogBox(Task task) {
        View dialogboxview = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_task,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogboxview);

        AlertDialog dialog = builder.create();

        EditText updateEditTextTitle = dialogboxview.findViewById(R.id.updateEditTextTitle);
        EditText updateEditTextDescription = dialogboxview.findViewById(R.id.updateEditTextDescription);
        updateDeadlineTextView = dialogboxview.findViewById(R.id.updateTextViewDeadline);

        NumberPicker updateReminderHours = dialogboxview.findViewById(R.id.updatenpReminderHours);
        NumberPicker updateReminderMinutes = dialogboxview.findViewById(R.id.updatenpReminderMinutes);
        NumberPicker updateFollowUpHours = dialogboxview.findViewById(R.id.updatenpFollowUpHours);
        NumberPicker updateFollowUpMinutes = dialogboxview.findViewById(R.id.updatenpFollowUpMinutes);
        NumberPicker updateCrossedHours = dialogboxview.findViewById(R.id.updatenpCrossedHours);
        NumberPicker updateCrossedMinutes = dialogboxview.findViewById(R.id.updatenpCrossedMinutes);

        updateDeadlineTextView.setOnClickListener(v->showDateTimePicker(updateDeadlineTextView));


        Button updateButton = dialogboxview.findViewById(R.id.updatebutton);
        Button discardButton = dialogboxview.findViewById(R.id.discardbutton);

        updateEditTextTitle.setText(task.getTitle());
        updateEditTextDescription.setText(task.getDescription());
        updateDeadlineTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm",task.getDeadlineMillis()));

        initializeNumberPicker(updateReminderHours,0,23,(int)(task.getReminderBeforeMillis()/(60*60*1000)));
        initializeNumberPicker(updateReminderMinutes,0,59,(int)((task.getReminderBeforeMillis()%(60*60*1000))/(60*1000)));
        initializeNumberPicker(updateFollowUpHours,0,23,(int)(task.getFollowUpFrequencyMillis()/(60*60*1000)));
        initializeNumberPicker(updateFollowUpMinutes,0,59,(int)((task.getFollowUpFrequencyMillis()%(60*60*1000))/(60*1000)));
        initializeNumberPicker(updateCrossedHours, 0, 23, (int)(task.getDeadlineCrossedMillis() / (60 * 60 * 1000)));
        initializeNumberPicker(updateCrossedMinutes, 0, 59, (int)((task.getDeadlineCrossedMillis() % (60 * 60 * 1000)) / (60 * 1000)));


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = updateEditTextTitle.getText().toString().trim();
                String description = updateEditTextDescription.getText().toString().trim();
                long deadlineMillis = selectedDeadline.getTimeInMillis();

                int reminderBeforeMs = (updateReminderHours.getValue() * 60 + updateReminderMinutes.getValue()) * 60 * 1000;
                int followUpMs = (updateFollowUpHours.getValue() * 60 + updateFollowUpMinutes.getValue()) * 60 * 1000;
                int crossedMs = (updateCrossedHours.getValue() * 60 + updateCrossedMinutes.getValue()) * 60 * 1000;

                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (deadlineMillis <= System.currentTimeMillis()) {
                    Toast.makeText(getContext(), "Please select a future deadline", Toast.LENGTH_SHORT).show();
                    return;
                }


                task.setTitle(title);
                task.setDescription(description);
                task.setDeadlineMillis(deadlineMillis);
                task.setReminderBeforeMillis(reminderBeforeMs);
                task.setFollowUpFrequencyMillis(followUpMs);
                task.setDeadlineCrossedMillis(crossedMs);

                DatabaseHelper db = new DatabaseHelper(getContext());
                int result = db.updateTask(task.getId(),task);
                if(result!=-1) {
                    Toast.makeText(getContext(), "Task Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadTaskFromDatabase();
                }
                else
                {
                    Toast.makeText(getContext(), "Error : FAILED TO UPDATE TASK", Toast.LENGTH_SHORT).show();
                }

            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showAddTaskDialogBox() {

        View dialogboxview = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogboxview);

        AlertDialog dialog = builder.create();

        EditText editTextTitle = dialogboxview.findViewById(R.id.editTextTitle);
        EditText editTextDescription = dialogboxview.findViewById(R.id.editTextDescription);
        deadlineTextView = dialogboxview.findViewById(R.id.textViewDeadline);

        NumberPicker npReminderHours = dialogboxview.findViewById(R.id.npReminderHours);
        NumberPicker npReminderMinutes = dialogboxview.findViewById(R.id.npReminderMinutes);
        NumberPicker npFollowUpHours = dialogboxview.findViewById(R.id.npFollowUpHours);
        NumberPicker npFollowUpMinutes = dialogboxview.findViewById(R.id.npFollowUpMinutes);
        NumberPicker npCrossedHours = dialogboxview.findViewById(R.id.npCrossedHours);
        NumberPicker npCrossedMinutes = dialogboxview.findViewById(R.id.npCrossedMinutes);

        initializeNumberPicker(npReminderHours, 0, 23,2);
        initializeNumberPicker(npReminderMinutes, 0, 59,0);
        initializeNumberPicker(npFollowUpHours, 0, 23,1);
        initializeNumberPicker(npFollowUpMinutes, 0, 59,0);
        initializeNumberPicker(npCrossedHours, 0, 23,1);
        initializeNumberPicker(npCrossedMinutes, 0, 59,0);

        deadlineTextView.setOnClickListener(v->showDateTimePicker(deadlineTextView));

        Button submitButton = dialogboxview.findViewById(R.id.submitbutton);
        Button cancelButton = dialogboxview.findViewById(R.id.cancelbutton);
        submitButton.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            long deadlineMillis = selectedDeadline.getTimeInMillis();


            int reminderBeforeMs = (npReminderHours.getValue() * 60 + npReminderMinutes.getValue()) * 60 * 1000;
            int followUpMs = (npFollowUpHours.getValue() * 60 + npFollowUpMinutes.getValue()) * 60 * 1000;
            int crossedMs = (npCrossedHours.getValue() * 60 + npCrossedMinutes.getValue()) * 60 * 1000;


            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (deadlineMillis <= System.currentTimeMillis()) {
                Toast.makeText(getContext(), "Please select a future deadline", Toast.LENGTH_SHORT).show();
                return;
            }

            Task newTask = new Task(title, description, deadlineMillis, false, reminderBeforeMs, followUpMs, crossedMs);

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            long result = dbHelper.addTask(newTask);

            if (result == -1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("ERROR");
                alertDialogBuilder.setMessage("Data cannot be Saved. Some Error occured");


                alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
//                dialog.dismiss();
            }
            else {
//                Toast.makeText(getContext(),
//                        "Title: " + title + "\n" +
//                                "Deadline: " + selectedDeadline.getTime() + "\n" +
//                                "Reminder: " + reminderBeforeMs / 60000 + " mins\n" +
//                                "Follow-up: " + followUpMs / 60000 + " mins\n" +
//                                "Crossed: " + crossedMs / 60000 + " mins",
//                        Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                loadTaskFromDatabase();
            }

        });

        cancelButton.setOnClickListener((v-> {
            dialog.dismiss();
        }));

        dialog.show();
    }



    private void initializeNumberPicker(NumberPicker picker, int min, int max,int defaultvalue) {
        picker.setMinValue(min);
        picker.setMaxValue(max);
        picker.setValue(defaultvalue);
        picker.setWrapSelectorWheel(true);
    }

    private void showDateTimePicker() {
        final Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            selectedDeadline.set(Calendar.YEAR, y);
            selectedDeadline.set(Calendar.MONTH, m);
            selectedDeadline.set(Calendar.DAY_OF_MONTH, d);

            int currentHour = current.get(Calendar.HOUR_OF_DAY);
            int currentMinute = current.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(requireContext(), (view1, selectedHour, selectedMinute) -> {
                selectedDeadline.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedDeadline.set(Calendar.MINUTE, selectedMinute);
                selectedDeadline.set(Calendar.SECOND, 0);
                selectedDeadline.set(Calendar.MILLISECOND, 0);

                // Check if selected deadline is in the past
                if (selectedDeadline.before(current)) {
                    Toast.makeText(requireContext(), "You can't select a past time.", Toast.LENGTH_SHORT).show();
                    deadlineTextView.setText("Select Deadline"); // Reset the text
                } else {
                    deadlineTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", selectedDeadline));
                }
            }, currentHour, currentMinute, false);

            timePicker.show();
        }, year, month, day);

        // Restrict date selection to today and future
        datePicker.getDatePicker().setMinDate(current.getTimeInMillis());

        datePicker.show();
    }

    private void showDateTimePicker(TextView targetTextView) {
        final Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            selectedDeadline.set(Calendar.YEAR, y);
            selectedDeadline.set(Calendar.MONTH, m);
            selectedDeadline.set(Calendar.DAY_OF_MONTH, d);

            int currentHour = current.get(Calendar.HOUR_OF_DAY);
            int currentMinute = current.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(requireContext(), (view1, selectedHour, selectedMinute) -> {
                selectedDeadline.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedDeadline.set(Calendar.MINUTE, selectedMinute);
                selectedDeadline.set(Calendar.SECOND, 0);
                selectedDeadline.set(Calendar.MILLISECOND, 0);

                if (selectedDeadline.before(current)) {
                    Toast.makeText(requireContext(), "You can't select a past time.", Toast.LENGTH_SHORT).show();
                    targetTextView.setText("Select Deadline"); // Reset
                } else {
                    targetTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", selectedDeadline));
                }
            }, currentHour, currentMinute, false);

            timePicker.show();
        }, year, month, day);

        datePicker.getDatePicker().setMinDate(current.getTimeInMillis());
        datePicker.show();
    }


}