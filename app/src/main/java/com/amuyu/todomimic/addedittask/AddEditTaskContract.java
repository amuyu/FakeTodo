package com.amuyu.todomimic.addedittask;


import com.amuyu.todomimic.BasePresenter;
import com.amuyu.todomimic.BaseView;
import com.amuyu.todomimic.tasks.domain.model.Task;

public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();
        void showTask(Task task);
        void showTasksList();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void saveTask(String title, String description);
    }
}
