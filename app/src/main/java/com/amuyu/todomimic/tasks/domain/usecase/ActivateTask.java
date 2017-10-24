package com.amuyu.todomimic.tasks.domain.usecase;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivateTask extends UseCase<ActivateTask.RequestValues, ActivateTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    @Inject
    public ActivateTask(@NonNull TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mTasksRepository.activateTask(requestValues.getActivateTask());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mActivateTask;

        public RequestValues(@NonNull String activateTask) {
            mActivateTask = checkNotNull(activateTask, "activateTask cannot be null!");
        }

        public String getActivateTask() {
            return mActivateTask;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue { }
}
