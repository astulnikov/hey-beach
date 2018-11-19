package com.dvipersquad.heybeach.beaches;

import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.auth.provider.AuthProvider;
import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.data.source.BeachDataSource;
import com.dvipersquad.heybeach.data.source.BeachRepository;

import java.util.List;

public class BeachesPresenter implements BeachesContract.Presenter {

    private final BeachRepository beachRepository;
    private final BeachesContract.View beachesView; //TODO Not clearing the ref to a view in general is a bad practice
    private final AuthProvider authProvider;

    private int currentPage = 0;

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
        if (beachesView.isActive()) {
            beachesView.toggleLoadingIndicator(true);
        }
        beachRepository.getBeaches(new BeachDataSource.LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                if (beachesView.isActive()) {
                    beachesView.toggleLoadingIndicator(false);
                    beachesView.showImages(beaches);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (beachesView.isActive()) {
                    beachesView.toggleLoadingIndicator(false);
                    beachesView.showLoadingImagesError();
                }
            }
        });
    }

    @Override
    public void openUserProfile() {
        authProvider.getUser(new AuthProvider.LoadUserCallback() {
            @Override
            public void onUserLoaded(User user, String token) {
                if (beachesView.isActive()) {
                    beachesView.showUserProfileUI();
                }
            }

            @Override
            public void onUserNotAvailable(String errorMessage) {
                if (beachesView.isActive()) {
                    beachesView.showUserLoginUI();
                }
            }
        });
    }

    @Override
    public void loadNextPage() {
        if (beachesView.isActive()) {
            beachesView.toggleLoadingIndicator(true);
        }
        currentPage++;
        beachRepository.getBeachesNextPage(currentPage, new BeachDataSource.LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                if (beachesView.isActive()) {
                    beachesView.toggleLoadingIndicator(false);
                    beachesView.showImages(beaches);
                }
            }

            @Override
            public void onDataNotAvailable() {
                currentPage--;
                if (beachesView.isActive()) {
                    beachesView.toggleLoadingIndicator(false);
                    beachesView.showLoadingImagesError();
                }
            }
        });
    }
}
