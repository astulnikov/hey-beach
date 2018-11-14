package com.dvipersquad.heybeach.beaches;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.register.RegisterActivity;
import com.dvipersquad.heybeach.userdetails.UserDetailsActivity;
import com.dvipersquad.heybeach.util.EndlessRecyclerViewScrollListener;
import com.dvipersquad.heybeach.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class BeachesFragment extends Fragment implements BeachesContract.View {

    private static final int COLUMNS = 3;
    private static final int CARD_SEPARATION = 10;
    private BeachesContract.Presenter presenter;

    private StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(COLUMNS, StaggeredGridLayoutManager.VERTICAL);

    private BeachesAdapter beachesAdapter;

    private ProgressBar progressBar;

    private boolean isLoading;

    private boolean isLastPage = false;

    private EndlessRecyclerViewScrollListener paginationListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            if (!isLoading && !isLastPage) {
                isLoading = true;
                presenter.loadNextPage();
            }
        }
    };

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
        progressBar = rootView.findViewById(R.id.progressBar);
        SpacesItemDecoration decoration = new SpacesItemDecoration(CARD_SEPARATION);
        RecyclerView recyclerBeaches = rootView.findViewById(R.id.recyclerBeaches);
        recyclerBeaches.setHasFixedSize(true);
        recyclerBeaches.setLayoutManager(staggeredGridLayoutManager);
        recyclerBeaches.addOnScrollListener(paginationListener);
        recyclerBeaches.addItemDecoration(decoration);
        recyclerBeaches.setAdapter(beachesAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoading = true;
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
        isLoading = false;
        beachesAdapter.addData(beaches);
    }

    @Override
    public void showLoadingImagesError() {
        if (getView() != null) {
            Snackbar.make(getView(), getString(R.string.no_more_images_available), Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void showUserProfileUI() {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showUserLoginUI() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void toggleLoadingIndicator(boolean active) {
        if (active) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
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
