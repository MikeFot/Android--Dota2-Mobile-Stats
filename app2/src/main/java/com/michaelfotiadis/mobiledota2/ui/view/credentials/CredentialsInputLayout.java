package com.michaelfotiadis.mobiledota2.ui.view.credentials;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.view.utils.ViewUtils;

public class CredentialsInputLayout extends LinearLayout {

    @LayoutRes
    private static final int LAYOUT = R.layout.credentials_layout;
    @ColorRes
    private static final int ICON_INFO = R.drawable.ic_info_black_24dp;
    @ColorRes
    private static final int ICON_CLEAR = R.drawable.ic_clear_black_24dp;
    @ColorRes
    private static final int DEFAULT_DISABLED_COLOR = R.color.grey_dark;
    @ColorRes
    private static final int DEFAULT_ENABLED_COLOR = R.color.primary_dark;
    @ColorRes
    private static final int DEFAULT_TINT_COLOR = R.color.primary;

    private CredentialsInputListener mListener;

    @ColorInt
    private int mColorDisabled;
    @ColorInt
    private int mColorEnabled;
    @ColorInt
    private int mColorTint;

    private TextInputEditText mUsernameEdit;
    private ImageView mUsernameStatusIcon;
    private ImageView mUsernameActionIcon;

    private ViewGroup mErrorLayout;
    private TextView mErrorText;


    public CredentialsInputLayout(final Context context) {
        super(context);
        this.init(context, null);
    }

    public CredentialsInputLayout(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public CredentialsInputLayout(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CredentialsInputLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        inflate(getContext(), LAYOUT, this);

        if (attrs != null) {
            final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CredentialsInputLayout, 0, 0);
            try {
                mColorDisabled = typedArray.getColor(R.styleable.CredentialsInputLayout_colorDisabled, getResources().getColor(DEFAULT_DISABLED_COLOR));
                mColorEnabled = typedArray.getColor(R.styleable.CredentialsInputLayout_colorEnabled, getResources().getColor(DEFAULT_ENABLED_COLOR));
                mColorTint = typedArray.getColor(R.styleable.CredentialsInputLayout_colorTint, getResources().getColor(DEFAULT_TINT_COLOR));
            } finally {
                typedArray.recycle();
            }
        } else {
            mColorEnabled = getResources().getColor(DEFAULT_DISABLED_COLOR);
            mColorEnabled = getResources().getColor(DEFAULT_ENABLED_COLOR);
        }

        mUsernameEdit = (TextInputEditText) findViewById(R.id.username_edit);
        mUsernameStatusIcon = (ImageView) findViewById(R.id.username_status_icon);
        mUsernameStatusIcon.setColorFilter(mColorDisabled);

        mUsernameActionIcon = (ImageView) findViewById(R.id.username_action_icon);
        mUsernameActionIcon.setImageResource(ICON_INFO);
        mUsernameActionIcon.setColorFilter(mColorTint);
        mUsernameActionIcon.setOnClickListener(getInfoActionListener());


        mErrorLayout = (ViewGroup) findViewById(R.id.layout_error);
        mErrorText = (TextView) findViewById(R.id.text_error);

        initTextWatchers();

    }

    @NonNull
    private OnClickListener getInfoActionListener() {

        return new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (TextUtils.isEmpty(mUsernameEdit.getText())) {
                    if (mListener != null) {
                        mListener.onInfoClicked((ImageView) v);
                    }
                } else {
                    mUsernameEdit.setText("");
                }
            }
        };

    }


    public void clearErrors() {
        ViewUtils.showViewAnimated(mErrorLayout, false);
    }


    public void setError(final CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            mErrorText.setText("");
            ViewUtils.showViewAnimated(mErrorLayout, false);
        } else {
            mErrorText.setText(message);
            ViewUtils.showViewAnimated(mErrorLayout, true);
        }
    }

    public void setUsername(final String username) {
        mUsernameEdit.setText(username);
    }

    public void setCredentialsInputListener(final CredentialsInputListener listener) {
        mListener = listener;
    }

    private void initTextWatchers() {

        mUsernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // NOOP
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                // NOOP
            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.length() > 0) {
                    mUsernameActionIcon.setImageResource(ICON_CLEAR);
                    mUsernameActionIcon.setColorFilter(mColorEnabled);
                } else {
                    mUsernameActionIcon.setImageResource(ICON_INFO);
                    mUsernameActionIcon.setColorFilter(mColorTint);
                }

                if (isUsernameValid()) {
                    mUsernameStatusIcon.setColorFilter(mColorEnabled);
                } else {
                    mUsernameStatusIcon.setColorFilter(mColorDisabled);
                }
                if (mListener != null) {
                    mListener.onReadyStateChanged(isLoginReady());
                }
            }
        });


    }


    private boolean isUsernameValid() {
        return mUsernameEdit != null && mUsernameEdit.getText().length() > 0;
    }

    private boolean isLoginReady() {

        return isUsernameValid();

    }

    public String getUsername() {
        return mUsernameEdit.getText().toString();
    }

    public interface CredentialsInputListener {

        void onKeypadSignIn(String username);

        void onReadyStateChanged(boolean isReady);

        void onInfoClicked(ImageView view);

    }


}
