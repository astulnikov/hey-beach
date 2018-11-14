package com.dvipersquad.heybeach.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.register.RegisterActivity;
import com.dvipersquad.heybeach.userdetails.UserDetailsActivity;

public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter presenter;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_frag, container, false);
        final TextInputEditText editTxtEmail = rootView.findViewById(R.id.editTxtEmail);
        final TextInputEditText editTxtPassword = rootView.findViewById(R.id.editTxtPassword);
        Button btnLogIn = rootView.findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (editTxtEmail.getText() != null && editTxtPassword.getText() != null) {
                    presenter.onUserLogin(editTxtEmail.getText().toString(), editTxtPassword.getText().toString());
                } else {
                    showErrorMessage(getString(R.string.review_fields_information));
                }
            }
        });
        Button btnRegister = rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onRegisterSelected();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showErrorMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showUserDetailsUI() {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showUserRegisterUI() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
