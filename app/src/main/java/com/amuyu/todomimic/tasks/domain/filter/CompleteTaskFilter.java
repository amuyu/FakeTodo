package com.amuyu.todomimic.tasks.domain.filter;


import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.ArrayList;
import java.util.List;

public class CompleteTaskFilter implements TaskFilter {
    @Override
    public List<Task> filter(List<Task> tasks) {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    @Override
    public Boolean filter(Task task) {
        return task.isCompleted();
    }
}
