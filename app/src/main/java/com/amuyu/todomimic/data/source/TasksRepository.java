package com.amuyu.todomimic.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return ;
        }

        if (mCacheIsDirty) {
            // Fetch new data from the network
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available If not, query the network.
            getTasksFromLocalDataSource(callback);
        }
    }

    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return ;
        }

        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        callback.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
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
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
        mTasksRemoteDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
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

    private void getTasksFromLocalDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getTasksFromRemoteDataSource(callback);
            }
        });
    }


    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
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
