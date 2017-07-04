package com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.web;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewFlipper;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.db.DbCallback;
import com.michaelfotiadis.dota2viewer.data.persistence.db.accessor.DbAccessor;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.core.base.fragment.BaseFragment;
import com.michaelfotiadis.dota2viewer.ui.core.toast.AppToast;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends BaseFragment {

    private static final String REALM_PARAM = "Mobile Stats for Dota2";

    @Inject
    DbAccessor mDbAccessor;
    @Inject
    JobScheduler mJobScheduler;

    @BindView(R.id.view_flipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.webview)
    WebView mWebView;

    @Override
    public void onAttach(final Context context) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventLifecycleListener().enable();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Next screen comes in from left.
        mViewFlipper.setInAnimation(getActivity(), R.anim.slide_in_top);
        // Current screen goes out from right.
        mViewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_bottom);


        mWebView.getSettings().setJavaScriptEnabled(true);

        // Constructing openid url request
        final String endpoint = "https://steamcommunity.com/openid/login?" +
                "openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.identity=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.mode=checkid_setup&" +
                "openid.ns=http://specs.openid.net/auth/2.0&" +
                "openid.realm=https://" + REALM_PARAM + "&" +
                "openid.return_to=https://" + REALM_PARAM + "/signin/";

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                if (url.equals(endpoint)) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(final WebView view, final String url,
                                      final Bitmap favicon) {
                final Uri Url = Uri.parse(url);

                if (Url.getAuthority().equals(REALM_PARAM.toLowerCase())) {
                    // That means that authentication is finished and the url contains user's id.
                    mWebView.stopLoading();
                    mViewFlipper.setDisplayedChild(0);

                    // Extracts user id.
                    final Uri userAccountUrl = Uri.parse(Url.getQueryParameter("openid.identity"));
                    final String userId = userAccountUrl.getLastPathSegment();
                    // Do whatever you want with the user's steam id

                    getNotificationController().showInfo("Fetching played data");
                    mJobScheduler.startFetchPlayersJob(userId);

                }
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
                mViewFlipper.setDisplayedChild(1);
            }
        });

        mWebView.loadUrl(endpoint);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedPlayersEvent event) {


        if (event.getError() == null && event.getPlayers() != null && !event.getPlayers().isEmpty()) {
            final PlayerSummary playerSummary = event.getPlayers().get(0);
            AppLog.d("User selected with ID= " + playerSummary.getSteamId());

            mDbAccessor.getPlayerAccessor().insert(playerSummary, new DbCallback() {
                @Override
                public void onSuccess() {
                    new UserPreferences(getContext()).writeCurrentUserId(playerSummary.getSteamId());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppLog.d("User written with ID " + getCurrentUserId());
                            AppToast.show(getContext(), String.format("User selected: %s", playerSummary.getPersonaName()));
                            getIntentDispatcher().openMainActivity(null);
                        }
                    });
                }

                @Override
                public void onFailure() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppToast.show(getContext(), "Something has gone wrong. Please try again.");
                            getActivity().finish();
                        }
                    });
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppToast.show(getContext(), "Something has gone wrong. Please try again.");
                    getActivity().finish();
                }
            });
        }

    }

    public static BaseFragment newInstance() {
        return new WebViewFragment();
    }


}
