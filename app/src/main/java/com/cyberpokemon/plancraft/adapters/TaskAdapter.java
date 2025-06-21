package com.cyberpokemon.plancraft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpokemon.plancraft.DatabaseHelper;
import com.cyberpokemon.plancraft.R;
import com.cyberpokemon.plancraft.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private Runnable refreshcallback;
    private int result;
    private OnTaskEditListener editListener;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public TaskAdapter(List<Task> taskList, Context context, Runnable refreshcallback) {
        this.taskList = taskList;
        this.context = context;
        this.refreshcallback = refreshcallback;
    }

    public TaskAdapter(List<Task> taskList, Context context, Runnable refreshcallback, OnTaskEditListener editListener) {
        this.taskList = taskList;
        this.context = context;
        this.refreshcallback = refreshcallback;
        this.editListener = editListener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskDeadline;
        Button btnComplete, btnDelete;
        ImageButton btnEdit;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle=itemView.findViewById(R.id.taskTitle);
            taskDescription=itemView.findViewById(R.id.taskDescription);
            taskDeadline=itemView.findViewById(R.id.taskDeadline);
            btnComplete=itemView.findViewById(R.id.buttonMarkAsComplete);
            btnDelete=itemView.findViewById(R.id.buttonDeleteTask);
            btnEdit=itemView.findViewById(R.id.buttonEditTask);
        }
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {

        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        holder.taskDeadline.setText("Deadline : "+android.text.format.DateFormat.format("yyyy-MM-dd HH:mm",task.getDeadlineMillis()));

        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
//                result = db.markTaskAsComplete(task.getId());
                result = db.markTaskAsComplete(context,task.getId());
                if(result!=-1) {
                    Toast.makeText(context, "Task Marked as Complete", Toast.LENGTH_SHORT).show();
                    refreshcallback.run();
                }
                else
                {
                    Toast.makeText(context, "Error : FAILED TO MARK COMPLETE TASK", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
//                result = db.deleteTask(task.getId());
                result = db.deleteTask(context,task.getId());
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

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editListener!=null)
                {
                    editListener.onTaskEdit(task);
                }
                else
                {
                    Toast.makeText(context, "Error : FAILED TO EDIT TASK", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
