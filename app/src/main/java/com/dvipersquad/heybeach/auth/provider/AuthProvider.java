package com.dvipersquad.heybeach.auth.provider;

import com.dvipersquad.heybeach.auth.User;

public interface AuthProvider {

    interface LoadUserCallback {

        void onUserLoaded(User user, String token);

        void onUserNotAvailable(String errorMessage);
    }

    void registerUser(String email, String password, LoadUserCallback callback);

    void loginUser(String email, String password, LoadUserCallback callback);

    void getUser(LoadUserCallback callback);

    void logoutUser();
}
