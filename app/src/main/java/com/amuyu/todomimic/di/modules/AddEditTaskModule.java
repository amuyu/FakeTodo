package com.amuyu.todomimic.di.modules;

import android.support.annotation.Nullable;

import com.amuyu.todomimic.addedittask.AddEditTaskContract;
import com.amuyu.todomimic.addedittask.AddEditTaskPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class AddEditTaskModule {

    private final AddEditTaskContract.View mAddTaskView;
    private final String taskId;

    public AddEditTaskModule(AddEditTaskContract.View mAddTaskView, String taskId) {
        this.mAddTaskView = mAddTaskView;
        this.taskId = taskId;
    }

    @Provides AddEditTaskContract.View provideView() {
        return mAddTaskView;
    }

    @Provides AddEditTaskContract.Presenter providePresenter(AddEditTaskPresenter presenter) {
        return presenter;
    }

    @Provides
    @Nullable
    String provideTaskId() {
        return taskId;
    }
}
