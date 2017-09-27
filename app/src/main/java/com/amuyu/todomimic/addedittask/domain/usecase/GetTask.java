package com.amuyu.todomimic.addedittask.domain.usecase;

import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksDataSource;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.tasks.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;


public class GetTask extends UseCase<GetTask.RequestValues, GetTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public GetTask(@NonNull TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String taskId = requestValues.getTaskId();
        mTasksRepository.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                ResponseValue responseValue = new ResponseValue(task);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
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

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Task mTask;

        public ResponseValue(@NonNull Task task) {
            mTask = checkNotNull(task, "task cannot be null!");
        }

        public Task getTask() {
            return mTask;
        }
    }

}
