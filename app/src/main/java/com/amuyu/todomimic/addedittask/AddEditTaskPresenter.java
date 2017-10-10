package com.amuyu.todomimic.addedittask;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.addedittask.domain.usecase.GetTask;
import com.amuyu.todomimic.addedittask.domain.usecase.SaveTask;
import com.amuyu.todomimic.tasks.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private final AddEditTaskContract.View mAddTaskView;
    private final SaveTask mSaveTask;
    private final GetTask mGetTask;

    @Nullable
    private String mTaskId;

    public AddEditTaskPresenter(@NonNull AddEditTaskContract.View addTaskView,
                                @NonNull SaveTask saveTask,
                                @NonNull GetTask getTask,
                                @NonNull String taskId) {
        this.mAddTaskView = checkNotNull(addTaskView);
        this.mSaveTask = checkNotNull(saveTask);
        this.mGetTask = checkNotNull(getTask);
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
        Logger.d("");
        if (mTaskId == null) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }

        mGetTask.execute(new GetTask.RequestValues(mTaskId))
                .subscribe(responseValue -> showTask(responseValue.getTask()));
    }

    private void createTask(final String title, final String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            SaveTask.RequestValues values = new SaveTask.RequestValues(newTask);
            mSaveTask.execute(values)
                    .subscribe(responseValue -> {
                        mAddTaskView.showTasksList();
                    });
        }
    }

    private void updateTask(String title, String description) {
        if (mTaskId == null) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        Task newTask = new Task(title ,description, mTaskId);
        mSaveTask.execute(new SaveTask.RequestValues(newTask))
                .subscribe(responseValue -> {
                    mAddTaskView.showTasksList();
                }, error -> {
                    showSaveError();
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
