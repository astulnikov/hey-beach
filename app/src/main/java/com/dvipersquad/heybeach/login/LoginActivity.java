package com.dvipersquad.heybeach.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dvipersquad.heybeach.Injection;
import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.util.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        getSupportActionBar().setTitle(R.string.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (loginFragment == null) {
            // Create the fragment
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), loginFragment, R.id.contentFrame);
        }

        // Create the presenter
        presenter = new LoginPresenter(
                Injection.provideAuthProvider(), loginFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Close current activity when home is pressed
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
