package com.amuyu.todomimic.taskdetail;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.UseCaseHandler;
import com.amuyu.todomimic.addedittask.domain.usecase.DeleteTask;
import com.amuyu.todomimic.addedittask.domain.usecase.GetTask;
import com.amuyu.todomimic.tasks.domain.model.Task;
import com.amuyu.todomimic.tasks.domain.usecase.ActivateTask;
import com.amuyu.todomimic.tasks.domain.usecase.CompleteTask;
import com.google.common.base.Strings;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TaskDetailContract.View mTaskDetailView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetTask mGetTask;
    private final CompleteTask mCompleteTask;
    private final ActivateTask mActivateTask;
    private final DeleteTask mDeleteTask;

    @Nullable
    private String mTaskId;

    @Inject public TaskDetailPresenter(@NonNull TaskDetailContract.View taskDetailView,
                               @NonNull UseCaseHandler useCaseHandler,
                               @NonNull GetTask getTask,
                               @NonNull CompleteTask completeTask,
                               @NonNull ActivateTask activateTask,
                               @NonNull DeleteTask mDeleteTask,
                               @Nullable String taskId) {
        this.mTaskDetailView = checkNotNull(taskDetailView);
        this.mUseCaseHandler = useCaseHandler;
        this.mGetTask = getTask;
        this.mCompleteTask = completeTask;
        this.mActivateTask = activateTask;
        this.mDeleteTask = mDeleteTask;
        this.mTaskId = taskId;
    }


    @Override
    public void start() {
        openTask();
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

        mUseCaseHandler.execute(mCompleteTask, new CompleteTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<CompleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(CompleteTask.ResponseValue response) {
                        mTaskDetailView.showTaskMarkedComplete();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void activateTask() {
        Logger.d("");
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }

        mUseCaseHandler.execute(mActivateTask, new ActivateTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<ActivateTask.ResponseValue>() {
                    @Override
                    public void onSuccess(ActivateTask.ResponseValue response) {
                        mTaskDetailView.showTaskMarkedActive();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void deleteTask() {
        mUseCaseHandler.execute(mDeleteTask, new DeleteTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<DeleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(DeleteTask.ResponseValue response) {
                        mTaskDetailView.showTaskDeleted();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void openTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return ;
        }

        mUseCaseHandler.execute(mGetTask, new GetTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<GetTask.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTask.ResponseValue response) {
                        Task task = response.getTask();

                        if(!mTaskDetailView.isActive()) {
                            return ;
                        }

                        showTask(task);
                    }

                    @Override
                    public void onError() {
                        if (!mTaskDetailView.isActive()) {
                            return ;
                        }
                        mTaskDetailView.showMissingTask();
                    }
                });
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
