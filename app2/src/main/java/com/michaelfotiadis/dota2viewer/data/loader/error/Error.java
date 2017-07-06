package com.michaelfotiadis.dota2viewer.data.loader.error;

/**
 * Custom Common Error object
 */
public class Error {

    private final ErrorKind mKind;
    private final int mHttpStatus;

    public Error(final ErrorKind kind) {
        mKind = kind;
        mHttpStatus = 0;
    }

    public Error(final ErrorKind kind, final int httpStatus) {
        mKind = kind;
        mHttpStatus = httpStatus;
    }

    public ErrorKind getKind() {
        return mKind;
    }

    public int getHttpStatus() {
        return mHttpStatus;
    }

    @Override
    public String toString() {
        return "Error{" +
                "mKind=" + mKind +
                ", mHttpStatus=" + mHttpStatus +
                '}';
    }
}
