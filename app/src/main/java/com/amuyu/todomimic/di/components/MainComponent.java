package com.amuyu.todomimic.di.components;


import android.content.Context;

import com.amuyu.todomimic.di.modules.MainModule;
import com.amuyu.todomimic.taskdetail.TaskDetailFragment;
import com.amuyu.todomimic.tasks.TasksFragment;

import dagger.Component;

@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(TasksFragment fragment);

    void inject(TaskDetailFragment fragment);

    Context context();
}
