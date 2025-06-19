package com.cyberpokemon.plancraft;

public class Task {

    private  String title;
    private  int id;
    private  String description;
    private long deadlineMillis;
    private  boolean isCompleted;

    private long reminderBeforeMillis;
    private long followUpFrequencyMillis;
    private long deadlineCrossedMillis;

    public Task(String title, String description, long deadlineMillis, boolean isCompleted, long reminderBforeMillis, long followUpFrequencyMillis, long deadlineCrossedMillis) {
        this.title = title;
        this.description = description;
        this.deadlineMillis = deadlineMillis;
        this.isCompleted = isCompleted;
        this.reminderBeforeMillis = reminderBforeMillis;
        this.followUpFrequencyMillis = followUpFrequencyMillis;
        this.deadlineCrossedMillis = deadlineCrossedMillis;
    }

    public Task(String title, String description, long deadlineMillis) {
        this.title = title;
        this.description = description;
        this.deadlineMillis = deadlineMillis;

        this.isCompleted=false;

        this.reminderBeforeMillis=2*60*60*1000L; //default 2 hours
        this.followUpFrequencyMillis=60*60*1000L; //default 1 hours
        this.deadlineCrossedMillis=60*60*1000L; //default 1 hours
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getReminderBeforeMillis() {
        return reminderBeforeMillis;
    }

    public void setReminderBeforeMillis(long reminderBforeMillis) {
        this.reminderBeforeMillis = reminderBforeMillis;
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
