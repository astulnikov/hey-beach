package com.dvipersquad.heybeach.register;

import android.text.TextUtils;
import android.util.Patterns;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;

public class RegisterPresenter implements RegisterContract.Presenter {

    private static final String ERROR_EMPTY = "Field required";
    private static final String ERROR_MIN_LENGTH = "Field needs to have at least 6 characters";
    private static final String ERROR_INVALID_EMAIL_FORMAT = "Email is not valid";
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
                if (registerView.isActive()) {
                    registerView.showUserDetailsUI();
                }
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                if (registerView.isActive()) {
                    registerView.showErrorMessage(errorMessage);
                }
            }
        });
    }

    @Override
    public void onLoginSelected() {
        if (registerView.isActive()) {
            registerView.showUserLoginUI();
        }
    }

    @Override
    public String validatePassword(String password) {
        return validateField(password);
    }

    @Override
    public String validateEmail(String email) {
        if (!isValidEmail(email)) {
            return ERROR_INVALID_EMAIL_FORMAT;
        } else {
            return validateField(email);
        }
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
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
