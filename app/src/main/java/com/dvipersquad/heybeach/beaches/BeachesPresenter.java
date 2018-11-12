package com.dvipersquad.heybeach.beaches;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;
import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.data.source.BeachDataSource;
import com.dvipersquad.heybeach.data.source.BeachRepository;

import java.util.List;

public class BeachesPresenter implements BeachesContract.Presenter {

    private final BeachRepository beachRepository;
    private final BeachesContract.View beachesView;
    private final AuthProvider authProvider;

    public BeachesPresenter(BeachRepository beachRepository, BeachesContract.View beachesView, AuthProvider authProvider) {
        this.beachRepository = beachRepository;
        this.beachesView = beachesView;
        this.beachesView.setPresenter(this);
        this.authProvider = authProvider;
    }

    @Override
    public void start() {
        loadImages();
    }

    @Override
    public void loadImages() {
        beachRepository.getBeaches(new BeachDataSource.LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                beachesView.showImages(beaches);
            }

            @Override
            public void onDataNotAvailable() {
                beachesView.showLoadingImagesError();
            }
        });
    }

    @Override
    public void openUserProfile() {
        authProvider.getUser(new AuthProvider.LoadUserCallback() {
            @Override
            public void onUserLoaded(User user, String token) {
                beachesView.showUserProfileUI();
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                beachesView.showUserLoginRegisterUI();
            }
        });
    }
}
