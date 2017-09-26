package com.amuyu.todomimic.data;


import android.content.Context;
import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCaseHandler;
import com.amuyu.todomimic.data.data.FakeTasksRemoteDataSource;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.data.source.local.TasksLocalDataSource;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.amuyu.todomimic.tasks.domain.usecase.GetTasks;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static List<Task> provideFakeTasks() {
        List<Task> tasks = new ArrayList<Task>(1);
        tasks.add(new Task("title", "description"));
        return tasks;
    }

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }

    public static GetTasks provideGetTasks(@NonNull Context context) {
        return new GetTasks(provideTasksRepository(context));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
