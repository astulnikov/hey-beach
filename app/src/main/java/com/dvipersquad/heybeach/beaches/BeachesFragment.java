package com.dvipersquad.heybeach.beaches;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.data.Beach;

import java.util.ArrayList;
import java.util.List;

public class BeachesFragment extends Fragment implements BeachesContract.View {

    private BeachesContract.Presenter presenter;

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private BeachesAdapter beachesAdapter;

    public BeachesFragment() {

    }

    public static BeachesFragment newInstance() {
        return new BeachesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        beachesAdapter = new BeachesAdapter(new ArrayList<Beach>());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.beaches_frag, container, false);
        RecyclerView recyclerBeaches = rootView.findViewById(R.id.recyclerBeaches);
        recyclerBeaches.setHasFixedSize(true);
        recyclerBeaches.setLayoutManager(linearLayoutManager);
        recyclerBeaches.setAdapter(beachesAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showUserProfile:
                presenter.openUserProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showImages(List<Beach> beaches) {
        beachesAdapter.replaceData(beaches);
    }

    @Override
    public void showLoadingImagesError() {
        Snackbar.make(getView(), getString(R.string.error_loading_images), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUserProfileUI() {
    }

    @Override
    public void showUserLoginRegisterUI() {
    }

    @Override
    public void setPresenter(BeachesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
