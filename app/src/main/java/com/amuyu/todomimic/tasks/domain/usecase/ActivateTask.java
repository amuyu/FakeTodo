package com.amuyu.todomimic.tasks.domain.usecase;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivateTask extends UseCase<ActivateTask.RequestValues, ActivateTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public ActivateTask(TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues values) {
        mTasksRepository.activateTask(values.getActivateTask());
        return Observable.just(new ResponseValue())
                .observeOn(AndroidSchedulers.mainThread());
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
