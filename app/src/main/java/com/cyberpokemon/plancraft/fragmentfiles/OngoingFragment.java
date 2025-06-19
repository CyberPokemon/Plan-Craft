package com.cyberpokemon.plancraft.fragmentfiles;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cyberpokemon.plancraft.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class OngoingFragment extends Fragment {
    private TextView deadlineTextView;

    private Calendar selectedDeadline = Calendar.getInstance();

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
        initializeNumberPicker(npReminderMinutes, 0, 59,00);
        initializeNumberPicker(npFollowUpHours, 0, 23,1);
        initializeNumberPicker(npFollowUpMinutes, 0, 59,00);
        initializeNumberPicker(npCrossedHours, 0, 23,1);
        initializeNumberPicker(npCrossedMinutes, 0, 59,00);

        deadlineTextView.setOnClickListener(v->showDateTimePicker());

        Button submitButton = dialogboxview.findViewById(R.id.submitbutton);
        Button cancelButton = dialogboxview.findViewById(R.id.cancelbutton);
        submitButton.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            long deadlineMillis = selectedDeadline.getTimeInMillis();

            int reminderBeforeMs = (npReminderHours.getValue() * 60 + npReminderMinutes.getValue()) * 60 * 1000;
            int followUpMs = (npFollowUpHours.getValue() * 60 + npFollowUpMinutes.getValue()) * 60 * 1000;
            int crossedMs = (npCrossedHours.getValue() * 60 + npCrossedMinutes.getValue()) * 60 * 1000;

            Toast.makeText(getContext(),
                    "Title: " + title + "\n" +
                            "Deadline: " + selectedDeadline.getTime() + "\n" +
                            "Reminder: " + reminderBeforeMs / 60000 + " mins\n" +
                            "Follow-up: " + followUpMs / 60000 + " mins\n" +
                            "Crossed: " + crossedMs / 60000 + " mins",
                    Toast.LENGTH_LONG).show();

            dialog.dismiss();

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

}