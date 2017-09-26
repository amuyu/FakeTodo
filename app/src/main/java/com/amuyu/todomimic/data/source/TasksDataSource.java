package com.amuyu.todomimic.data.source;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TasksDataSource {

    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);
        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);
    void saveTask(@NonNull Task task);
    void deleteAllTasks();
    void refreshTasks();
}
