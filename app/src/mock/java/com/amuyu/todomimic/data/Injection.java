package com.amuyu.todomimic.data;


import android.content.Context;
import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCaseHandler;
import com.amuyu.todomimic.addedittask.domain.usecase.DeleteTask;
import com.amuyu.todomimic.addedittask.domain.usecase.GetTask;
import com.amuyu.todomimic.addedittask.domain.usecase.SaveTask;
import com.amuyu.todomimic.data.data.FakeTasksRemoteDataSource;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.data.source.local.TasksLocalDataSource;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.amuyu.todomimic.tasks.domain.usecase.ActivateTask;
import com.amuyu.todomimic.tasks.domain.usecase.CompleteTask;
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

    public static SaveTask provideSaveTask(@NonNull Context context) {
        return new SaveTask(provideTasksRepository(context));
    }

    public static GetTask provideGetTask(@NonNull Context context) {
        return new GetTask(provideTasksRepository(context));
    }
    public static CompleteTask provideCompleteTask(@NonNull Context context) {
        return new CompleteTask(provideTasksRepository(context));
    }

    public static ActivateTask provideActivateTask(@NonNull Context context) {
        return new ActivateTask(provideTasksRepository(context));
    }

    public static DeleteTask provideDeleteTask(@NonNull Context context) {
        return new DeleteTask(provideTasksRepository(context));
    }


    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
