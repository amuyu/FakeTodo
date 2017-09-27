package com.amuyu.todomimic.addedittask;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.UseCaseHandler;
import com.amuyu.todomimic.addedittask.domain.usecase.GetTask;
import com.amuyu.todomimic.addedittask.domain.usecase.SaveTask;
import com.amuyu.todomimic.tasks.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private final AddEditTaskContract.View mAddTaskView;
    private final SaveTask mSaveTask;
    private final GetTask mGetTask;
    private final UseCaseHandler mUseCaseHandler;

    @Nullable
    private String mTaskId;

    public AddEditTaskPresenter(@NonNull AddEditTaskContract.View addTaskView,
                                @NonNull SaveTask saveTask,
                                @NonNull GetTask getTask,
                                @NonNull UseCaseHandler useCaseHandler,
                                @NonNull String taskId) {
        this.mAddTaskView = checkNotNull(addTaskView);
        this.mSaveTask = checkNotNull(saveTask);
        this.mGetTask = checkNotNull(getTask);
        this.mUseCaseHandler = useCaseHandler;
        this.mTaskId = taskId;

        mAddTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mTaskId != null) {
            populateTask();
        }
    }

    @Override
    public void saveTask(String title, String description) {
        Logger.d("title:"+title+",des:"+description);

        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }

    @Override
    public void populateTask() {
        if (mTaskId == null) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }

        mUseCaseHandler.execute(mGetTask, new GetTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<GetTask.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTask.ResponseValue response) {
                        showTask(response.getTask());
                    }

                    @Override
                    public void onError() {
                        showEmptyTaskError();
                    }
                });
    }

    private void createTask(final String title, final String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            SaveTask.RequestValues values = new SaveTask.RequestValues(newTask);
            mUseCaseHandler.execute(mSaveTask, values, new UseCase.UseCaseCallback<SaveTask.ResponseValue>() {
                @Override
                public void onSuccess(SaveTask.ResponseValue response) {
                    mAddTaskView.showTasksList();
                }

                @Override
                public void onError() {
                    showSaveError();
                }
            });
        }
    }

    private void updateTask(String title, String description) {
        if (mTaskId == null) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        Task newTask = new Task(title ,description, mTaskId);
        mUseCaseHandler.execute(mSaveTask, new SaveTask.RequestValues(newTask),
                new UseCase.UseCaseCallback<SaveTask.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveTask.ResponseValue response) {
                        mAddTaskView.showTasksList();
                    }

                    @Override
                    public void onError() {
                        showSaveError();
                    }
                });
    }

    private void showTask(Task task) {
        mAddTaskView.showTask(task);
    }

    private void showSaveError() {
        // Show error, log, etc
    }

    private void showEmptyTaskError() {
        if (mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError();
        }
    }
}
