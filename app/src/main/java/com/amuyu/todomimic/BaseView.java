package com.amuyu.todomimic;


public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
