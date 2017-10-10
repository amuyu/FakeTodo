package com.amuyu.todomimic.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.logger.Logger;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concreate implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;
    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;

    boolean mCacheIsDirty = false;

    public TasksRepository(@NonNull TasksDataSource mTasksRemoteDataSource,
                           @NonNull TasksDataSource mTasksLocalDataSource) {
        this.mTasksRemoteDataSource = checkNotNull(mTasksRemoteDataSource);
        this.mTasksLocalDataSource = checkNotNull(mTasksLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param mTasksRemoteDataSource
     * @param mTasksLocalDataSource
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(TasksDataSource mTasksRemoteDataSource,
                                              TasksDataSource mTasksLocalDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new TasksRepository(mTasksRemoteDataSource, mTasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Task>> getTasks() {
        if (mCachedTasks != null && !mCacheIsDirty) {
            return Observable.from(mCachedTasks.values()).toList();
        } else if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }

        if (mCacheIsDirty) {
            return getTasksFromRemoteDataSource();
        } else {
            return getTasksFromLocalDataSource();
        }
    }

    @Override
    public Observable<Task> getTask(@NonNull String taskId) {
        Logger.d("");
        checkNotNull(taskId);

        Task cachedTask = getTaskWithId(taskId);
        if (cachedTask != null) {
            return Observable.create(subscriber -> subscriber.onNext(cachedTask));
        }

        String finalTaskId = taskId;
        return mTasksLocalDataSource.getTask(taskId)
                    .flatMap(task -> {
                        if (task == null)
                            return mTasksRemoteDataSource.getTask(finalTaskId);
                        else
                            return Observable.just(task);
                    });

    }


    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);

        Task activateTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activateTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }

        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> map = it.next();
            if(map.getValue().isCompleted())
                it.remove();
        }
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
        mTasksRemoteDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksRemoteDataSource.deleteTask(taskId);
        mTasksLocalDataSource.deleteTask(taskId);

        mCachedTasks.remove(taskId);
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTasksLocalDataSource.saveTask(task);
        }
    }

    private Observable<List<Task>> getTasksFromLocalDataSource() {
        return mTasksLocalDataSource.getTasks()
                .doOnNext(this::refreshCache);
    }

    private Observable<List<Task>> getTasksFromRemoteDataSource() {
        return mTasksRemoteDataSource.getTasks()
                .doOnNext(tasks -> {
                    refreshCache(tasks);
                    refreshLocalDataSource(tasks);
                });
    }


    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
