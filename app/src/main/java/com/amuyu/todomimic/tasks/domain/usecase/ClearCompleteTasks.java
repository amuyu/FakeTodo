package com.amuyu.todomimic.tasks.domain.usecase;

import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import javax.inject.Inject;


public class ClearCompleteTasks extends UseCase<ClearCompleteTasks.RequestValues, ClearCompleteTasks.ResponseValue> {

    private final TasksRepository mTasksRepository;

    @Inject
    public ClearCompleteTasks(@NonNull TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mTasksRepository.clearCompletedTasks();
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue { }
}
