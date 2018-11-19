package com.dvipersquad.heybeach.login;

import com.dvipersquad.heybeach.BasePresenter;
import com.dvipersquad.heybeach.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showErrorMessage(String message);

        void showUserDetailsUI();

        void showUserRegisterUI();

    }

    interface Presenter extends BasePresenter {

        void onUserLogin(String email, String password);

        void onRegisterSelected();

    }
}
