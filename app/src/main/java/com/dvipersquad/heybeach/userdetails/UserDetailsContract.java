package com.dvipersquad.heybeach.userdetails;

import com.dvipersquad.heybeach.BasePresenter;
import com.dvipersquad.heybeach.BaseView;
import com.dvipersquad.heybeach.auth.User;

public interface UserDetailsContract {

    interface View extends BaseView<Presenter> {

        void showUserLogOutUI();

        void showUser(User user);

    }

    interface Presenter extends BasePresenter {

        void onUserLogOut();
    }
}
