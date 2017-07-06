package com.michaelfotiadis.dota2viewer.ui.view.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableLayout extends RelativeLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private boolean checked = false;

    @SuppressLint("NewApi")
    public CheckableLayout(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CheckableLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckableLayout(final Context context) {
        super(context);
        init();
    }

    private void init() {
        /*setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
				setChecked(!isChecked());
			}
		});*/
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(final boolean checked) {
        this.checked = checked;

        refreshDrawableState();

        //Propagate to childs
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(checked);
            }
        }
    }

    @Override
    protected int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);
        //if (changed)
        //	getLayoutParams().height = r - l;
    }
}
