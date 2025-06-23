package com.cyberpokemon.plancraft.notification;

import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.cyberpokemon.plancraft.Task;

import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    public static void scheduleTaskNotifications(Context context, Task task) {

        WorkManager workManager = WorkManager.getInstance(context);
        long now = System.currentTimeMillis();

        String tag = "task_"+ task.getId();

        workManager.cancelAllWorkByTag(tag);

        // 1. Reminder before deadline
        long reminderTime = task.getDeadlineMillis() - task.getReminderBeforeMillis();
        if (reminderTime > now  && task.getReminderBeforeMillis()!=0) {
            enqueueOneTimeWork(context, task, "REMINDER_BEFORE", reminderTime - now, tag);
        }

        // 2. Follow-ups between reminder and deadline
        long followUpStart = reminderTime + task.getFollowUpFrequencyMillis();
        while (followUpStart < task.getDeadlineMillis() && task.getFollowUpFrequencyMillis()!=0) {
            enqueueOneTimeWork(context, task, "FOLLOW_UP", followUpStart - now, tag);
            followUpStart += task.getFollowUpFrequencyMillis();
        }

        // 3. At deadline
        if (task.getDeadlineMillis() > now) {
            enqueueOneTimeWork(context, task, "DEADLINE_REACHED", task.getDeadlineMillis() - now, tag);
        }

        // 4. Post-deadline repeating reminder
        if(task.getDeadlineCrossedMillis()!=0)
        {
            enqueueRepeatingOverdueWork(context, task, tag);
        }
    }

    public static void cancelTaskNotifications(Context context,Task task) {
        WorkManager.getInstance(context).cancelAllWorkByTag("task_" + task.getId());
    }


    private static void enqueueOneTimeWork(Context context, Task task, String type, long delay, String tag) {
        Data data = new Data.Builder()
                .putLong("task_id", task.getId())
                .putString("title", task.getTitle())
                .putString("type", type)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TaskNotificationWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    private static void enqueueRepeatingOverdueWork(Context context, Task task, String tag) {
//        if (System.currentTimeMillis() < task.getDeadlineMillis()) return;
        long delay = task.getDeadlineMillis() - System.currentTimeMillis();
        if (delay > 0) {
            // Wait until deadline is passed
            enqueueOneTimeWork(context, task, "OVERDUE", delay+task.getDeadlineCrossedMillis(), tag);
            return;
        }


        Data data = new Data.Builder()
                .putLong("task_id", task.getId())
                .putString("title", task.getTitle())
                .putString("type", "OVERDUE")
                .build();

//        PeriodicWorkRequest repeatingWork = new PeriodicWorkRequest.Builder(
//                TaskNotificationWorker.class,
//                task.getDeadlineCrossedMillis(), TimeUnit.MILLISECONDS
//        )
//                .setInputData(data)
//                .addTag(tag)
//                .build();

        PeriodicWorkRequest repeatingWork = new PeriodicWorkRequest.Builder(
                TaskNotificationWorker.class,
                Math.max(task.getDeadlineCrossedMillis(), 15 * 60 * 1000), TimeUnit.MILLISECONDS
        )
                .setInputData(data)
                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueue(repeatingWork);
    }
}
