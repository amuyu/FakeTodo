package com.amuyu.todomimic.di.modules;


import android.content.Context;

import com.amuyu.todomimic.TodoApp;
import com.amuyu.todomimic.data.data.FakeTasksRemoteDataSource;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.data.source.local.TasksLocalDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private final TodoApp todoApp;

    public MainModule(TodoApp todoApp) {
        this.todoApp = todoApp;
    }

    @Provides
    Context provideApplicationContext() {
        return todoApp;
    }

    @Provides
    TasksRepository provideTasksRepository(Context context) {
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }
}
