package com.amuyu.todomimic.di.components;


import android.content.Context;

import com.amuyu.todomimic.di.modules.MainModule;
import com.amuyu.todomimic.di.modules.TasksModule;
import com.amuyu.todomimic.taskdetail.TaskDetailFragment;

import dagger.Component;


@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(TaskDetailFragment fragment);
    Context context();
    TasksComponent tasksComponent(TasksModule module);
}
