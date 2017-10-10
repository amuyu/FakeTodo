package com.amuyu.todomimic.tasks.domain.filter;


import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

public interface TaskFilter {
    List<Task> filter(List<Task> tasks);
    Boolean filter(Task task);
}
