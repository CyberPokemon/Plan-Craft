package com.cyberpokemon.plancraft.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TaskNotificationWorker extends Worker {


    public TaskNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        long taskId = getInputData().getLong("task_id", -1);
        String title = getInputData().getString("title");
        String description = getInputData().getString("description");
        String type = getInputData().getString("type");

        if(title==null || type==null)
        {
            return Result.failure();
        }

        String message;
        switch (type) {
            case "REMINDER_BEFORE":
                message = "Upcoming Task : " + title;
                break;
            case "FOLLOW_UP":
                message = "Reminder to continue: " + title;
                break;
            case "DEADLINE_REACHED":
                message = "Deadline reached for: " + title;
                break;
            case "OVERDUE":
                message = "Still incomplete: " + title;
                break;
            default:
                message = "Task: " + title;
                break;
        }

        NotificationHelper.showNotification(getApplicationContext(), "Task Reminder", message);
        return Result.success();
    }
}
