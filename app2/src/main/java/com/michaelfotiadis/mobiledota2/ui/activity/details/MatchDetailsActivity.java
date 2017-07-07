package com.michaelfotiadis.mobiledota2.ui.activity.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.activity.details.graph.DotaMatchGraphFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.details.graph.DotaMatchPlayerFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.details.map.DotaMatchMapFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.details.match.DotaMatchDetailsFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;

public class MatchDetailsActivity extends BaseActivity {

    private static final String PAYLOAD_EXTRA = "extra";
    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final int CONTENT_ID = R.id.content_frame;
    private MatchContainer mMatch;

    public static Intent newInstance(final Context context, final MatchContainer match) {
        final Intent intent = new Intent(context, MatchDetailsActivity.class);
        intent.putExtra(PAYLOAD_EXTRA, match);
        return intent;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_details:
                    replaceContentFragment(DotaMatchDetailsFragment.newInstance(mMatch), CONTENT_ID, FRAGMENT_TAG);
                    return true;
                case R.id.navigation_map:
                    replaceContentFragment(DotaMatchMapFragment.newInstance(mMatch), CONTENT_ID, FRAGMENT_TAG);
                    return true;
                case R.id.navigation_player:
                    replaceContentFragment(DotaMatchPlayerFragment.newInstance(mMatch), CONTENT_ID, FRAGMENT_TAG);
                    return true;
                case R.id.navigation_graphs:
                    replaceContentFragment(DotaMatchGraphFragment.newInstance(mMatch), CONTENT_ID, FRAGMENT_TAG);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        mMatch = getIntent().getParcelableExtra(PAYLOAD_EXTRA);

        setTitle("Match " + mMatch.getMatchDetails().getMatchId());

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_details);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_match_details;
    }

}
