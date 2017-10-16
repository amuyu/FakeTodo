package com.amuyu.todomimic.data.source.remote;


import android.support.annotation.NonNull;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.data.source.TasksDataSource;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource INSTANCE;
    private DatabaseReference databaseRef;

    public TasksRemoteDataSource() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null) INSTANCE = new TasksRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        databaseRef.child("tasks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Task> tasks = new ArrayList<>();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Logger.d("toString:"+snapshot.getValue(Task.class));
                    tasks.add(snapshot.getValue(Task.class));
                }
                callback.onTasksLoaded(tasks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        Logger.d("");
        databaseRef.child("tasks").child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onTaskLoaded(dataSnapshot.getValue(Task.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        databaseRef.child("tasks").child(task.getId()).setValue(task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        databaseRef.child("tasks").child(task.getId()).setValue(completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not required
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activateTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        databaseRef.child("tasks").child(task.getId()).setValue(activateTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required
    }

    @Override
    public void clearCompletedTasks() {
        databaseRef.child("tasks").orderByChild("completed")
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void deleteAllTasks() {
        databaseRef.child("tasks").removeValue();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        databaseRef.child("tasks").child(taskId).removeValue();
    }

    @Override
    public void refreshTasks() {
        // Not required
    }
}
