package com.amuyu.todomimic.tasks.domain.usecase;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class ClearCompleteTasks extends UseCase<ClearCompleteTasks.RequestValues, ClearCompleteTasks.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public ClearCompleteTasks(TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues values) {
        mTasksRepository.clearCompletedTasks();
        return Observable.just(new ResponseValue())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue { }
}
