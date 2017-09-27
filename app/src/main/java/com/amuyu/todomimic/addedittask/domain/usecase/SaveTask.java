package com.amuyu.todomimic.addedittask.domain.usecase;

import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.tasks.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;


public class SaveTask extends UseCase<SaveTask.RequestValues, SaveTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public SaveTask(@NonNull TasksRepository tasksRepository) {
        this.mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Task task = requestValues.getTask();
        mTasksRepository.saveTask(task);

        getUseCaseCallback().onSuccess(new ResponseValue(task));
    }




    public static final class RequestValues implements UseCase.RequestValues {

        private final Task mTask;

        public RequestValues(@NonNull Task task) {
            mTask = checkNotNull(task, "task cannot be null!");
        }

        public Task getTask() {
            return mTask;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Task mTask;

        public ResponseValue(@NonNull Task task) {
            mTask = checkNotNull(task, "task cannot be null!");
        }

        public Task getTask() {
            return mTask;
        }
    }
}
