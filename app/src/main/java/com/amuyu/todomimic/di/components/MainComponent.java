package com.amuyu.todomimic.di.components;


import android.content.Context;

import com.amuyu.todomimic.di.modules.AddEditTaskModule;
import com.amuyu.todomimic.di.modules.MainModule;
import com.amuyu.todomimic.di.modules.TaskDetailModule;
import com.amuyu.todomimic.di.modules.TasksModule;

import dagger.Component;


@Component(modules = MainModule.class)
public interface MainComponent {
    Context context();
    TasksComponent tasksComponent(TasksModule module);
    AddEditTaskComponent addEditTaskComponent(AddEditTaskModule module);
    TaskDetailComponent taskDetailComponent(TaskDetailModule module);
}
