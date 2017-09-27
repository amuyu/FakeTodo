package com.amuyu.todomimic.tasks;

import com.amuyu.todomimic.BasePresenter;
import com.amuyu.todomimic.BaseView;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TaskContract {

    interface View extends BaseView<Presenter> {

        void showAddTask();

        void showActiveFilterLabel();
        void showTasks(List<Task> tasks);

        void showNoTasks();
        void showNoActiveTasks();
        void showNoCompletedTasks();

        void showCompletedFilterLabel();
        void showAllFilterLabel();

        void showFilteringPopUpMenu();

        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        void addNewTask();
    }
}
