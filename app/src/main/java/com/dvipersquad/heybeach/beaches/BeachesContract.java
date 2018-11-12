package com.dvipersquad.heybeach.beaches;

import com.dvipersquad.heybeach.BasePresenter;
import com.dvipersquad.heybeach.BaseView;
import com.dvipersquad.heybeach.data.Beach;

import java.util.List;

public interface BeachesContract {

    interface View extends BaseView<Presenter> {

        void showImages(List<Beach> beaches);

        void showLoadingImagesError();

        void showUserProfileUI();

        void showUserLoginRegisterUI();

    }

    interface Presenter extends BasePresenter {

        void loadImages();

        void openUserProfile();

    }
}
