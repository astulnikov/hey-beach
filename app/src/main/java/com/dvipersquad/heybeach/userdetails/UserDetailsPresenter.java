package com.dvipersquad.heybeach.userdetails;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;

public class UserDetailsPresenter implements UserDetailsContract.Presenter {

    private AuthProvider authProvider;

    private UserDetailsContract.View userDetailsView;

    public UserDetailsPresenter(AuthProvider authProvider, UserDetailsContract.View userDetailsView) {
        this.authProvider = authProvider;
        this.userDetailsView = userDetailsView;
        this.userDetailsView.setPresenter(this);
    }

    @Override
    public void onUserLogOut() {
        authProvider.logoutUser();
        if (userDetailsView.isActive()) {
            userDetailsView.showUserLogOutUI();
        }
    }

    @Override
    public void start() {
        getUser();
    }

    private void getUser() {
        authProvider.getUser(new AuthProvider.LoadUserCallback() {
            @Override
            public void onUserLoaded(User user, String token) {
                if (userDetailsView.isActive()) {
                    userDetailsView.showUser(user);
                }
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                if (userDetailsView.isActive()) {
                    userDetailsView.showUserLogOutUI();
                }
            }
        });
    }
}
