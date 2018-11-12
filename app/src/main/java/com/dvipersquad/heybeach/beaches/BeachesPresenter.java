package com.dvipersquad.heybeach.beaches;

import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.data.source.BeachDataSource;
import com.dvipersquad.heybeach.data.source.BeachRepository;

import java.util.List;

public class BeachesPresenter implements BeachesContract.Presenter {

    private final BeachRepository beachRepository;
    private final BeachesContract.View beachesView;

    public BeachesPresenter(BeachRepository beachRepository, BeachesContract.View beachesView) {
        this.beachRepository = beachRepository;
        this.beachesView = beachesView;
        this.beachesView.setPresenter(this);
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
}
