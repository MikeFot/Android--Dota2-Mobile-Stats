package com.michaelfotiadis.dota2viewer.data.persistence.error;

import java.io.Serializable;

/**
 *
 */
public final class UiDataLoadError implements Serializable {

    private final ErrorKind mKind;
    private final CharSequence mMessage;
    private final boolean mRecoverable;

    public UiDataLoadError(final CharSequence message,
                           final ErrorKind kind,
                           final boolean recoverable) {
        this.mMessage = message;
        this.mKind = kind;
        this.mRecoverable = recoverable;
    }

    public ErrorKind getKind() {
        return mKind;
    }

    public CharSequence getMessage() {
        return mMessage;
    }

    public boolean isRecoverable() {
        return mRecoverable;
    }

    @Override
    public String toString() {
        return String.format("UiDataLoadError{kind=%s, message=%s, recoverable=%s}", mKind, mMessage, mRecoverable);
    }

    public enum ErrorKind {
        UNKNOWN,
        AUTHENTICATION,
        NOT_FOUND,
        GENERIC,
        SERVER_ERROR,
        NO_NETWORK,
        NO_DATA,
        PERMISSION_DENIED
    }

}
