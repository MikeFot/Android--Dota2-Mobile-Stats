package com.michaelfotiadis.dota2viewer.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;

/**
 * Created by Michael on 25/05/2015.
 * Updated 2017
 */
public class ThreeBarView extends View {

    private final ShapeDrawable[] mDrawables = new ShapeDrawable[3];
    private int mColorOne, mColorTwo, mColorThree;
    private int mValueOne, mValueTwo, mValueThree;

    public ThreeBarView(final Context context) {
        this(context, null);
    }

    public ThreeBarView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeBarView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThreeBarView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mDrawables[0] = new ShapeDrawable(new RectShape());
        mDrawables[0].getPaint().setColor(mColorOne);
        mDrawables[1] = new ShapeDrawable(new RectShape());
        mDrawables[1].getPaint().setColor(mColorTwo);
        mDrawables[2] = new ShapeDrawable(new RectShape());
        mDrawables[2].getPaint().setColor(mColorThree);

        // set the edit mode values
        if (isInEditMode()) {
            setValueOne(5);
            setValueTwo(3);
            setValueThree(12);
            setColorOne(getResources().getColor(R.color.md_red_500));
            setColorTwo(getResources().getColor(R.color.md_light_blue_200));
            setColorThree(getResources().getColor(R.color.md_green_700));
        }
    }

    public int getColorOne() {
        return mColorOne;
    }

    public void setColorOne(final int mColorOne) {
        this.mColorOne = mColorOne;
        mDrawables[0].getPaint().setColor(this.mColorOne);
    }

    public int getColorTwo() {
        return mColorTwo;
    }

    public void setColorTwo(final int mColorTwo) {
        this.mColorTwo = mColorTwo;
        mDrawables[1].getPaint().setColor(this.mColorTwo);
    }

    public int getColorThree() {
        return mColorThree;
    }

    public void setColorThree(final int mColorThree) {
        this.mColorThree = mColorThree;
        mDrawables[2].getPaint().setColor(this.mColorThree);
    }

    public int getValueOne() {
        return mValueOne;
    }

    public void setValueOne(final int mValueOne) {
        this.mValueOne = mValueOne;
    }

    public int getValueTwo() {
        return mValueTwo;
    }

    public void setValueTwo(final int mValueTwo) {
        this.mValueTwo = mValueTwo;
    }

    public int getValueThree() {
        return mValueThree;
    }

    public void setValueThree(final int mValueThree) {
        this.mValueThree = mValueThree;
        if (mValueThree == 0) {
            this.mValueThree = 1;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        int x = 0;
        final int y = 0;

        float sum = mValueOne + mValueTwo + mValueThree;
        if (sum == 0) {
            sum = 1;
        }

        float step = width * mValueOne / sum;
        final Drawable drOne = mDrawables[0];
        drOne.setBounds(x, y, (int) (x + step), y + height);
        drOne.draw(canvas);
        x += step;

        step = width * mValueTwo / sum;
        final Drawable drTwo = mDrawables[1];
        drTwo.setBounds(x, y, (int) (x + step), y + height);
        drTwo.draw(canvas);
        x += step;

        step = width * mValueThree / sum;
        final Drawable drThree = mDrawables[2];
        drThree.setBounds(x, y, (int) (x + step), y + height);
        drThree.draw(canvas);

        this.invalidate();
    }

}
