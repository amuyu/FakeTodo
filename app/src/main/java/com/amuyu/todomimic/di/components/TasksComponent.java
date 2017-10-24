package com.amuyu.todomimic.di.components;


import com.amuyu.todomimic.di.modules.TasksModule;
import com.amuyu.todomimic.tasks.TasksFragment;

import dagger.Subcomponent;

@Subcomponent(modules = TasksModule.class)
public interface TasksComponent {
    void inject(TasksFragment fragment);

}
