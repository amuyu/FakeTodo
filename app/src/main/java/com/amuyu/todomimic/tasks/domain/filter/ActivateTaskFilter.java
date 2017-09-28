package com.amuyu.todomimic.tasks.domain.filter;


import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ActivateTaskFilter implements TaskFilter {

    @Override
    public List<Task> filter(List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>();
        for(Task task : tasks) {
            if (task.isActive()) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}
