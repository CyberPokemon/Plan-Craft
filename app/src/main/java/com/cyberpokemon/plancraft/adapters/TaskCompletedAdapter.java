package com.cyberpokemon.plancraft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpokemon.plancraft.DatabaseHelper;
import com.cyberpokemon.plancraft.R;
import com.cyberpokemon.plancraft.Task;

import java.util.List;

public class TaskCompletedAdapter extends RecyclerView.Adapter<TaskCompletedAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private Runnable refreshcallback;
    private int result;

    public TaskCompletedAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public TaskCompletedAdapter(List<Task> taskList, Context context, Runnable refreshcallback) {
        this.taskList = taskList;
        this.context = context;
        this.refreshcallback = refreshcallback;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskDeadline,taskCompletedOn;
        Button btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle=itemView.findViewById(R.id.taskTitleCompleted);
            taskDescription=itemView.findViewById(R.id.taskDescriptionCompleted);
            taskDeadline=itemView.findViewById(R.id.taskDeadlineCompleted);
            taskCompletedOn=itemView.findViewById(R.id.taskCompletedOn);
            btnDelete=itemView.findViewById(R.id.buttonDeleteTaskCompleted);
        }
    }

    @NonNull
    @Override
    public TaskCompletedAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_task,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCompletedAdapter.TaskViewHolder holder, int position) {
        Task task= taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        holder.taskDeadline.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm",task.getDeadlineMillis()));
        if (task.getCompletedTimeMillis() > 0) {
            holder.taskCompletedOn.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", task.getCompletedTimeMillis()));
        } else {
            holder.taskCompletedOn.setText("N/A");
        }

        holder.taskCompletedOn.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm",task.getCompletedTimeMillis()));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
                result = db.deleteTask(task.getId());
                if(result!=-1)
                {
                    Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
                    refreshcallback.run();
                }
                else
                {
                    Toast.makeText(context, "Error : FAILED TO DELETE TASK", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
