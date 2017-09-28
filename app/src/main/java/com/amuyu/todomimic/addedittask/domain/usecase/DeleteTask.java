package com.amuyu.todomimic.addedittask.domain.usecase;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteTask extends UseCase<DeleteTask.RequestValues, DeleteTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public DeleteTask(TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mTasksRepository.deleteTask(requestValues.getTaskId());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String mTaskId;

        public RequestValues(@NonNull String taskId) {
            mTaskId = checkNotNull(taskId, "taskId cannot be null!");
        }

        public String getTaskId() {
            return mTaskId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue { }
}

