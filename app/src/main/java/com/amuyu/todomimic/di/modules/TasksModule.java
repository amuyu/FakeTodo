package com.amuyu.todomimic.di.modules;


import com.amuyu.todomimic.tasks.TaskContract;
import com.amuyu.todomimic.tasks.TasksPresenter;

import dagger.Module;
import dagger.Provides;

@Module public class TasksModule {
    private final TaskContract.View view;

    public TasksModule(TaskContract.View view) {
        this.view = view;
    }

    @Provides TaskContract.View provideTaskContractView() {
        return view;
    }

    @Provides TaskContract.Presenter provideTasksPresenter(TasksPresenter presenter) {
        return presenter;
    }

}
