package com.amuyu.todomimic.data.source;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TasksDataSource {

    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);
        void onDataNotAvailable();
    }

    interface GetTaskCallback {
        void onTaskLoaded(Task task);
        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);
    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);
    void saveTask(@NonNull Task task);
    void completeTask(@NonNull Task task);
    void completeTask(@NonNull String taskId);
    void activateTask(@NonNull Task task);
    void activateTask(@NonNull String taskId);
    void deleteAllTasks();
    void deleteTask(@NonNull String taskId);
    void refreshTasks();
}
