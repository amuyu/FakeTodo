package com.amuyu.todomimic.tasks.domain.filter;


import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.ArrayList;
import java.util.List;

public class FilterAllTaskFilter implements TaskFilter{
    @Override
    public List<Task> filter(List<Task> tasks) {
        return new ArrayList<>(tasks);
    }
}
