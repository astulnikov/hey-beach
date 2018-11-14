package com.dvipersquad.heybeach;

public interface BaseView<T> {

    void setPresenter(T presenter);

    boolean isActive();
}
