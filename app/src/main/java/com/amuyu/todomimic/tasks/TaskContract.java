package com.amuyu.todomimic.tasks;

import com.amuyu.todomimic.BasePresenter;
import com.amuyu.todomimic.BaseView;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TaskContract {

    interface View extends BaseView<Presenter> {
        void showActiveFilterLabel();
        void showTasks(List<Task> tasks);

        void showNoTasks();
        void showNoActiveTasks();
        void showNoCompletedTasks();

        void showCompletedFilterLabel();
        void showAllFilterLabel();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {
        void loadTasks(boolean forceUpdate);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);
    }
}
