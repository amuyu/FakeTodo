package com.amuyu.todomimic.di.components;


import com.amuyu.todomimic.addedittask.AddEditTaskFragment;
import com.amuyu.todomimic.di.modules.AddEditTaskModule;

import dagger.Subcomponent;

@Subcomponent(modules = AddEditTaskModule.class)
public interface AddEditTaskComponent {
    void inject(AddEditTaskFragment fragment);
}
