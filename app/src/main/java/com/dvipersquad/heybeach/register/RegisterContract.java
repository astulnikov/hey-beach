package com.dvipersquad.heybeach.register;

import com.dvipersquad.heybeach.BasePresenter;
import com.dvipersquad.heybeach.BaseView;

public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void showErrorMessage(String message);

        void showUserDetailsUI();

        void showUserLoginUI();

    }

    interface Presenter extends BasePresenter {

        void onUserRegister(String email, String password);

        void onLoginSelected();

        String validatePassword(String password);

        String validateEmail(String email);
    }
}
