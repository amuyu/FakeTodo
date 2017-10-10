package com.amuyu.todomimic.taskdetail;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.addedittask.domain.usecase.DeleteTask;
import com.amuyu.todomimic.addedittask.domain.usecase.GetTask;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.amuyu.todomimic.tasks.domain.usecase.ActivateTask;
import com.amuyu.todomimic.tasks.domain.usecase.CompleteTask;
import com.google.common.base.Strings;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TaskDetailContract.View mTaskDetailView;
    private final GetTask mGetTask;
    private final CompleteTask mCompleteTask;
    private final ActivateTask mActivateTask;
    private final DeleteTask mDeleteTask;

    @Nullable
    private String mTaskId;

    private CompositeSubscription mSubscriptions;

    public TaskDetailPresenter(@NonNull TaskDetailContract.View taskDetailView,
                               @NonNull GetTask getTask,
                               @NonNull CompleteTask completeTask,
                               @NonNull ActivateTask activateTask,
                               @NonNull DeleteTask mDeleteTask,
                               @Nullable String taskId) {
        this.mTaskDetailView = checkNotNull(taskDetailView);
        this.mGetTask = getTask;
        this.mCompleteTask = completeTask;
        this.mActivateTask = activateTask;
        this.mDeleteTask = mDeleteTask;
        this.mTaskId = taskId;

        mTaskDetailView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void start() {
        openTask();
    }

    @Override
    public void onDestroy() {
        mSubscriptions.clear();
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }
        mTaskDetailView.showEditTask(mTaskId);
    }

    @Override
    public void completeTask() {
        Logger.d("");
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }

        Subscription subscription = mCompleteTask.execute(new CompleteTask.RequestValues(mTaskId))
                .subscribe(responseValue -> {
                    mTaskDetailView.showTaskMarkedComplete();
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void activateTask() {
        Logger.d("");
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }

        Subscription subscription = mActivateTask.execute(new ActivateTask.RequestValues(mTaskId))
                .subscribe(responseValue -> {
                    mTaskDetailView.showTaskMarkedActive();
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void deleteTask() {
        Subscription subscription = mDeleteTask.execute(new DeleteTask.RequestValues(mTaskId))
                .subscribe(responseValue -> {
                    mTaskDetailView.showTaskDeleted();
                });
        mSubscriptions.add(subscription);
    }

    private void openTask() {
        Logger.d("");
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }

        Subscription subscription = mGetTask.execute(new GetTask.RequestValues(mTaskId))
                .filter(task -> mTaskDetailView.isActive())
                .subscribe(responseValue -> {
                    Task task = responseValue.getTask();
                    if (task == null) {
                        mTaskDetailView.showMissingTask();
                    }
                    else {
                        showTask(task);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }

        mTaskDetailView.showCompletionStatus(task.isCompleted());
    }


}
