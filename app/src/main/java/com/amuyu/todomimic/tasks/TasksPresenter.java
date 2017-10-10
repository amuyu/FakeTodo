package com.amuyu.todomimic.tasks;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.addedittask.AddEditTaskActivity;
import com.amuyu.todomimic.data.source.TasksDataSource;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.amuyu.todomimic.tasks.domain.usecase.ActivateTask;
import com.amuyu.todomimic.tasks.domain.usecase.ClearCompleteTasks;
import com.amuyu.todomimic.tasks.domain.usecase.CompleteTask;
import com.amuyu.todomimic.tasks.domain.usecase.GetTasks;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksPresenter implements TaskContract.Presenter {

    private final TaskContract.View mTasksView;
    private final GetTasks mGetTasks;
    private final CompleteTask mCompleteTask;
    private final ActivateTask mActivateTask;
    private final ClearCompleteTasks mClearCompleteTasks;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;


    public TasksPresenter(@NonNull TaskContract.View tasksView,
                          @NonNull GetTasks mGetTasks,
                          @NonNull CompleteTask mCompleteTask,
                          @NonNull ActivateTask mActivateTask,
                          @NonNull ClearCompleteTasks clearCompleteTasks) {
        this.mTasksView = checkNotNull(tasksView);
        this.mGetTasks = checkNotNull(mGetTasks);
        this.mCompleteTask = mCompleteTask;
        this.mActivateTask = mActivateTask;
        this.mClearCompleteTasks = clearCompleteTasks;

        this.mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (requestCode == AddEditTaskActivity.REQUEST_ADD_TASK &&
                resultCode == Activity.RESULT_OK) {
            mTasksView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate, true);
    }

    @Override
    public void clearCompletedTasks() {
        Logger.d("");

        mClearCompleteTasks.execute(new ClearCompleteTasks.RequestValues())
                .subscribe(responseValue -> {
                    mTasksView.showCompletedTasksCleared();
                    loadTasks(false, false);
                }, error -> {
                    mTasksView.showLoadingTasksError();
                });
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");

        mCompleteTask.execute(new CompleteTask.RequestValues(completedTask.getId()))
                .subscribe(responseValue -> {
                    mTasksView.showTaskMarkedComplete();
                    loadTasks(false, false);
                }, error -> mTasksView.showLoadingTasksError());
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "completedTask cannot be null!");

        mActivateTask.execute(new ActivateTask.RequestValues(activeTask.getId()))
                .subscribe(responseValue -> {
                    mTasksView.showTaskMarkedActive();
                    loadTasks(false, false);
                }, error -> mTasksView.showLoadingTasksError());
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        mGetTasks.execute(new GetTasks.RequestValues(forceUpdate, mCurrentFiltering))
                .subscribe(responseValue -> processTask(responseValue.getTasks()));
    }

    private void processTask(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTasksView.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
            default:
                mTasksView.showNoTasks();
                break;
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLabel();
                break;
            default:
                mTasksView.showAllFilterLabel();
                break;
        }
    }
}
