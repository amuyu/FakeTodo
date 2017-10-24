package com.amuyu.todomimic.di.components;

import com.amuyu.todomimic.di.modules.TaskDetailModule;
import com.amuyu.todomimic.taskdetail.TaskDetailFragment;

import dagger.Subcomponent;

@Subcomponent(modules = TaskDetailModule.class)
public interface TaskDetailComponent {
    void inject(TaskDetailFragment fragment);
}
