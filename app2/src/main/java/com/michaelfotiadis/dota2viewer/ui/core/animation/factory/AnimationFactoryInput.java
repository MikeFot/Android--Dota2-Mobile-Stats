package com.michaelfotiadis.dota2viewer.ui.core.animation.factory;

/**
 *
 */

public final class AnimationFactoryInput {

    private final String mPath;
    private final String mFileNamePrefix;
    private final int mFrameCount;
    private final long mTotalAnimationTime;

    private AnimationFactoryInput(final Builder builder) {
        mPath = builder.mPath;
        mFileNamePrefix = builder.mFileNamePrefix;
        mFrameCount = builder.mFrameCount;
        mTotalAnimationTime = builder.mTotalAnimationTime;
    }

    public String getPath() {
        return mPath;
    }

    public String getFileNamePrefix() {
        return mFileNamePrefix;
    }

    public int getFrameCount() {
        return mFrameCount;
    }

    public long getTotalAnimationTime() {
        return mTotalAnimationTime;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String mPath;
        private String mFileNamePrefix;
        private int mFrameCount;
        private long mTotalAnimationTime;

        private Builder() {
        }

        public Builder withPath(final String val) {
            mPath = val;
            return this;
        }

        public Builder withFileNamePrefix(final String val) {
            mFileNamePrefix = val;
            return this;
        }

        public Builder withFrameCount(final int val) {
            mFrameCount = val;
            return this;
        }

        public Builder withTotalAnimationTime(final long val) {
            mTotalAnimationTime = val;
            return this;
        }

        public AnimationFactoryInput build() {
            return new AnimationFactoryInput(this);
        }
    }
}
