package com.dvipersquad.heybeach.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dvipersquad.heybeach.R;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter presenter;

    private boolean isLogin = true;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_frag, container, false);
        final TextInputEditText txtInputEmail = rootView.findViewById(R.id.editTxtEmail);
        txtInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputEmail.setError(presenter.validateEmail(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final TextInputEditText txtInputPassword = rootView.findViewById(R.id.editTxtPassword);
        txtInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputPassword.setError(presenter.validatePassword(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final TextInputEditText txtInputRepeatPassword = rootView.findViewById(R.id.txtInputRepeatPassword);
        txtInputRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtInputPassword.getText().toString().equals(charSequence.toString())) {
                    txtInputRepeatPassword.setError(getString(R.string.password_should_match));
                } else {
                    txtInputRepeatPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button btnRegister = rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtInputEmail.getError() == null && txtInputEmail.getText() != null &&
                        txtInputPassword.getError() == null && txtInputPassword.getText() != null &&
                        txtInputRepeatPassword.getError() == null && txtInputRepeatPassword.getText() != null) {
                    presenter.onUserRegister(txtInputEmail.getText().toString(), txtInputPassword.getText().toString());
                } else {
                    showErrorMessage(getString(R.string.review_fields_information));
                }
            }
        });

        Button btnLogIn = rootView.findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginSelected();
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
        this.showErrorMessage("User logged");
    }

    @Override
    public void showUserLoginUI() {

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
