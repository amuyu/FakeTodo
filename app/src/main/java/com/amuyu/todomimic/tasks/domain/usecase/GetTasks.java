package com.amuyu.todomimic.tasks.domain.usecase;


import android.support.annotation.NonNull;

import com.amuyu.todomimic.UseCase;
import com.amuyu.todomimic.data.source.TasksDataSource;
import com.amuyu.todomimic.data.source.TasksRepository;
import com.amuyu.todomimic.tasks.TasksFilterType;
import com.amuyu.todomimic.tasks.domain.filter.FilterFactory;
import com.amuyu.todomimic.tasks.domain.filter.TaskFilter;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetTasks extends UseCase<GetTasks.RequestValues, GetTasks.ResponseValue> {

    private final TasksRepository mTasksRepository;
    private final FilterFactory mFilterFactory;

    public GetTasks(@NonNull TasksRepository tasksRepository, FilterFactory filterFactory) {
        this.mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
        this.mFilterFactory = filterFactory;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if(values.isForceUpdate()) {
            mTasksRepository.refreshTasks();
        }

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {

                TaskFilter taskFilter = mFilterFactory.create(values.getCurrentFiltering());
                List<Task> tasksFiltered = taskFilter.filter(tasks);

                ResponseValue responseValue = new ResponseValue(tasksFiltered);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final TasksFilterType mCurrentFiltering;
        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate, @NonNull TasksFilterType currentFiltering) {
            mForceUpdate = forceUpdate;
            mCurrentFiltering = checkNotNull(currentFiltering, "currentFiltering cannot be null!");
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public TasksFilterType getCurrentFiltering() {
            return mCurrentFiltering;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Task> mTasks;

        public ResponseValue(@NonNull List<Task> tasks) {
            mTasks = checkNotNull(tasks, "tasks cannot be null!");
        }

        public List<Task> getTasks() {
            return mTasks;
        }
    }
}
