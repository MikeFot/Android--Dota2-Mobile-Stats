package com.michaelfotiadis.mobiledota2.ui.core.animation.flip;

/**
 * Taken from com.tekle.oss.android.animation
 */
public enum FlipDirection {
    LEFT_RIGHT,
    RIGHT_LEFT,
    TOP_BOTTOM,
    BOTTOM_TOP;

    public float getStartDegreeForFirstView() {
        return 0;
    }

    public float getStartDegreeForSecondView() {
        switch (this) {
            case LEFT_RIGHT:
            case TOP_BOTTOM:
                return -90;
            case RIGHT_LEFT:
            case BOTTOM_TOP:
                return 90;
            default:
                return 0;
        }
    }

    public float getEndDegreeForFirstView() {
        switch (this) {
            case LEFT_RIGHT:
            case TOP_BOTTOM:
                return 90;
            case RIGHT_LEFT:
            case BOTTOM_TOP:
                return -90;
            default:
                return 0;
        }
    }

    public float getEndDegreeForSecondView() {
        return 0;
    }

    public FlipDirection theOtherDirection() {
        switch (this) {
            case LEFT_RIGHT:
                return RIGHT_LEFT;
            case TOP_BOTTOM:
                return BOTTOM_TOP;
            case RIGHT_LEFT:
                return LEFT_RIGHT;
            case BOTTOM_TOP:
                return TOP_BOTTOM;
            default:
                return null;
        }
    }
};