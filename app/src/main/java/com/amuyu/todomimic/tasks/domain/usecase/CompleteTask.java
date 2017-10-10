package com.amuyu.todomimic.tasks.domain.usecase;

import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.base.Preconditions.checkNotNull;


public class CompleteTask extends UseCase<CompleteTask.RequestValues, CompleteTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public CompleteTask(@NonNull TasksRepository tasksRepository) {
        this.mTasksRepository = checkNotNull(tasksRepository,"tasksRepository cannot be null");
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues values) {
        mTasksRepository.completeTask(values.getCompletedTask());
        return Observable.just(new ResponseValue())
                    .observeOn(AndroidSchedulers.mainThread());
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mCompletedTask;

        public RequestValues(@NonNull String completedTask) {
            mCompletedTask = checkNotNull(completedTask, "completedTask cannot be null!");
        }

        public String getCompletedTask() {
            return mCompletedTask;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }
}
