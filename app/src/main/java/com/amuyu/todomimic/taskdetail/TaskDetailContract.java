package com.amuyu.todomimic.taskdetail;

import com.amuyu.todomimic.BasePresenter;
import com.amuyu.todomimic.BaseView;

public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {
        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showMissingTask();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showEditTask(String taskId);

        void showTaskDeleted();

        boolean isActive();
    }


    interface Presenter extends BasePresenter {
        void editTask();

        void completeTask();

        void activateTask();

        void deleteTask();
    }
}
