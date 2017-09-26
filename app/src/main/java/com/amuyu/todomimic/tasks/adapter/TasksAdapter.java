package com.amuyu.todomimic.tasks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amuyu.todomimic.R;
import com.amuyu.todomimic.tasks.domain.model.Task;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksAdapter extends BaseAdapter {

    private List<Task> mTasks;

    public TasksAdapter(List<Task> tasks) {
        setList(tasks);
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<Task> tasks) {
        mTasks = checkNotNull(tasks);
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Task getItem(int i) {
        return mTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View rowView = view;
        ViewHolder holder = null;
        if(rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.task_item, viewGroup, false);
            holder = new ViewHolder(rowView);

        } else {
            holder = (ViewHolder)view.getTag();
        }



        final Task task = getItem(i);
        holder.titleView.setText(task.getTitleForList());

        holder.completeCheckBox.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            rowView.setBackground(viewGroup.getContext()
                    .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
        } else {
            rowView.setBackground(viewGroup.getContext()
                    .getResources().getDrawable(R.drawable.touch_feedback));
        }

        rowView.setTag(holder);
        return rowView;
    }

    public class ViewHolder {

        TextView titleView;
        CheckBox completeCheckBox;

        public ViewHolder(View view) {
            titleView = (TextView)view.findViewById(R.id.title);
            completeCheckBox = (CheckBox)view.findViewById(R.id.complete);
        }
    }

}
