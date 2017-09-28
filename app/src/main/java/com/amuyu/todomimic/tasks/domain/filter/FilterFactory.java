package com.amuyu.todomimic.tasks.domain.filter;


import com.amuyu.todomimic.tasks.TasksFilterType;

import java.util.HashMap;
import java.util.Map;

public class FilterFactory {

    private static final Map<TasksFilterType, TaskFilter> mFilters = new HashMap<>();

    public FilterFactory() {
        mFilters.put(TasksFilterType.ALL_TASKS, new FilterAllTaskFilter());
        mFilters.put(TasksFilterType.ACTIVE_TASKS, new ActivateTaskFilter());
        mFilters.put(TasksFilterType.COMPLETED_TASKS, new CompleteTaskFilter());
    }

    public TaskFilter create(TasksFilterType filterType) {
        return mFilters.get(filterType);
    }
}
