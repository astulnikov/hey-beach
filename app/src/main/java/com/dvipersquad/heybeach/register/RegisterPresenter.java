package com.dvipersquad.heybeach.register;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;

public class RegisterPresenter implements RegisterContract.Presenter {

    private static final String ERROR_EMPTY = "Field required";
    private static final String ERROR_MIN_LENGTH = "Field needs to have at least 6 characters";
    private AuthProvider authProvider;

    private RegisterContract.View registerView;

    public RegisterPresenter(AuthProvider authProvider, RegisterContract.View registerView) {
        this.authProvider = authProvider;
        this.registerView = registerView;
        this.registerView.setPresenter(this);
    }

    @Override
    public void onUserRegister(String email, String password) {
        authProvider.registerUser(email, password, new AuthProvider.LoadUserCallback() {
            @Override
            public void onUserLoaded(User user, String token) {
                registerView.showUserDetailsUI();
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                registerView.showErrorMessage(errorMessage);
            }
        });
    }

    @Override
    public void onLoginSelected() {
        registerView.showUserLoginUI();
    }

    @Override
    public String validatePassword(String password) {
        return validateField(password);
    }

    @Override
    public String validateEmail(String email) {
        return validateField(email);
    }

    @Override
    public void start() {

    }

    private String validateField(String field) {
        if (field == null || field.isEmpty()) {
            return ERROR_EMPTY;
        }

        if (field.length() < 6) {
            return ERROR_MIN_LENGTH;
        }

        return null;
    }
}
