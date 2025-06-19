package com.cyberpokemon.plancraft;

public class Task {

    private  String title;
    private  String description;
    private long deadlineMillis;
    private  boolean isCompleted;

    private long reminderBforeMillis;
    private long followUpFrequencyMillis;
    private long deadlineCrossedMillis;

    public Task(String title, String description, long deadlineMillis, boolean isCompleted, long reminderBforeMillis, long followUpFrequencyMillis, long deadlineCrossedMillis) {
        this.title = title;
        this.description = description;
        this.deadlineMillis = deadlineMillis;
        this.isCompleted = isCompleted;
        this.reminderBforeMillis = reminderBforeMillis;
        this.followUpFrequencyMillis = followUpFrequencyMillis;
        this.deadlineCrossedMillis = deadlineCrossedMillis;
    }

    public Task(String title, String description, long deadlineMillis) {
        this.title = title;
        this.description = description;
        this.deadlineMillis = deadlineMillis;

        this.isCompleted=false;

        this.reminderBforeMillis=2*60*60*1000L; //default 2 hours
        this.followUpFrequencyMillis=60*60*1000L; //default 1 hours
        this.deadlineCrossedMillis=60*60*1000L; //default 1 hours
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDeadlineMillis() {
        return deadlineMillis;
    }

    public void setDeadlineMillis(long deadlineMillis) {
        this.deadlineMillis = deadlineMillis;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getReminderBforeMillis() {
        return reminderBforeMillis;
    }

    public void setReminderBforeMillis(long reminderBforeMillis) {
        this.reminderBforeMillis = reminderBforeMillis;
    }

    public long getFollowUpFrequencyMillis() {
        return followUpFrequencyMillis;
    }

    public void setFollowUpFrequencyMillis(long followUpFrequencyMillis) {
        this.followUpFrequencyMillis = followUpFrequencyMillis;
    }

    public long getDeadlineCrossedMillis() {
        return deadlineCrossedMillis;
    }

    public void setDeadlineCrossedMillis(long deadlineCrossedMillis) {
        this.deadlineCrossedMillis = deadlineCrossedMillis;
    }
}
