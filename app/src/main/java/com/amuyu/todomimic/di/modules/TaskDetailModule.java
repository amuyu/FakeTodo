package com.amuyu.todomimic.di.modules;


import android.support.annotation.Nullable;

import com.amuyu.todomimic.taskdetail.TaskDetailContract;
import com.amuyu.todomimic.taskdetail.TaskDetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskDetailModule {

    private final TaskDetailContract.View view;
    private final String taskId;

    public TaskDetailModule(TaskDetailContract.View view, String taskId) {
        this.view = view;
        this.taskId = taskId;
    }

    @Provides
    TaskDetailContract.View provideView() {
        return view;
    }

    @Provides
    TaskDetailContract.Presenter providePresenter(TaskDetailPresenter presenter) {
        return presenter;
    }

    @Provides
    @Nullable
    String provideTaskId() {
        return taskId;
    }
}
