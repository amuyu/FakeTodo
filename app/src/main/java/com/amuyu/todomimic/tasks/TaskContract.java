package com.amuyu.todomimic.tasks;

import android.support.annotation.NonNull;

import com.amuyu.todomimic.BasePresenter;
import com.amuyu.todomimic.BaseView;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TaskContract {

    interface View extends BaseView<Presenter> {

        void showAddTask();

        void showActiveFilterLabel();
        void showTasks(List<Task> tasks);

        void showTaskMarkedComplete();
        void showTaskMarkedActive();

        void showNoTasks();
        void showNoActiveTasks();
        void showNoCompletedTasks();

        void showCompletedFilterLabel();
        void showAllFilterLabel();

        void showFilteringPopUpMenu();

        void showSuccessfullySavedMessage();
        void showTaskDetailsUi(String taskId);

        void showCompletedTasksCleared();

        void showLoadingTasksError();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestedTask);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);
    }
}
