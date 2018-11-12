package com.dvipersquad.heybeach.beaches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dvipersquad.heybeach.Injection;
import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.util.ActivityUtils;

public class BeachesActivity extends AppCompatActivity {

    private BeachesPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beaches_act);

        BeachesFragment beachesFragment =
                (BeachesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (beachesFragment == null) {
            // Create the fragment
            beachesFragment = BeachesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), beachesFragment, R.id.contentFrame);
        }

        // Create the presenter
        presenter = new BeachesPresenter(
                Injection.provideBeachRepository(), beachesFragment);
    }
}
