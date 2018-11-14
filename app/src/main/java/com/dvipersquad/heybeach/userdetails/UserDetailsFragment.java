package com.dvipersquad.heybeach.userdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.auth.User;

public class UserDetailsFragment extends Fragment implements UserDetailsContract.View {

    private UserDetailsContract.Presenter presenter;

    private TextView txtUserEmail;

    public UserDetailsFragment() {
    }

    public static UserDetailsFragment newInstance() {
        return new UserDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.userdetails_frag, container, false);
        txtUserEmail = rootView.findViewById(R.id.txtUserEmail);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logged_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userLogout:
                presenter.onUserLogOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showUserLogOutUI() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void showUser(User user) {
        txtUserEmail.setText(user.getEmail());
    }

    @Override
    public void setPresenter(UserDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
