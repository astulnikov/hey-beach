package com.dvipersquad.heybeach.login;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;

public class LoginPresenter implements LoginContract.Presenter {

    private AuthProvider authProvider;

    private LoginContract.View loginView;

    public LoginPresenter(AuthProvider authProvider, LoginContract.View loginView) {
        this.authProvider = authProvider;
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }

    @Override
    public void onUserLogin(String email, String password) {
        authProvider.loginUser(email, password, new AuthProvider.LoadUserCallback() {
            @Override
            public void onUserLoaded(User user, String token) {
                if (loginView.isActive()) {
                    loginView.showUserDetailsUI();
                }
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                if (loginView.isActive()) {
                    loginView.showErrorMessage(errorMessage);
                }
            }
        });
    }

    @Override
    public void onRegisterSelected() {
        if (loginView.isActive()) {
            loginView.showUserRegisterUI();
        }
    }

    @Override
    public void start() {

    }
}
