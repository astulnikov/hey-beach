package com.dvipersquad.heybeach.userdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dvipersquad.heybeach.Injection;
import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.util.ActivityUtils;

public class UserDetailsActivity extends AppCompatActivity {

    private UserDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetails_act);

        getSupportActionBar().setTitle(R.string.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserDetailsFragment userDetailsFragment =
                (UserDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (userDetailsFragment == null) {
            // Create the fragment
            userDetailsFragment = UserDetailsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), userDetailsFragment, R.id.contentFrame);
        }

        // Create the presenter
        presenter = new UserDetailsPresenter(
                Injection.provideAuthProvider(), userDetailsFragment);
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
