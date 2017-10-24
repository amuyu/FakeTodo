package com.amuyu.todomimic.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amuyu.todomimic.R;
import com.amuyu.todomimic.TodoApp;
import com.amuyu.todomimic.di.modules.AddEditTaskModule;
import com.amuyu.todomimic.tasks.domain.model.Task;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    @Inject public AddEditTaskContract.Presenter mPresenter;
    private TextView mTitle;
    private TextView mDescription;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDagger();
    }

    private void initializeDagger() {
        TodoApp app = (TodoApp)getActivity().getApplication();
        String taskId = null;
        if(getArguments()!= null) taskId = getArguments().getString(ARGUMENT_EDIT_TASK_ID);
        app.getMainComponent().addEditTaskComponent(new AddEditTaskModule(this, taskId))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.addtask_frag, container, false);
        mTitle = (TextView)root.findViewById(R.id.add_task_title);
        mDescription = (TextView)root.findViewById(R.id.add_task_description);


        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString());
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTask(Task task) {
        if (isActive()) {
            mTitle.setText(task.getTitle());
            mDescription.setText(task.getDescription());
        }
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
