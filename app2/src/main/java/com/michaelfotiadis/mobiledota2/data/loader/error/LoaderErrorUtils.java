package com.michaelfotiadis.mobiledota2.data.loader.error;

import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.steam.net.callback.Reason;

public final class LoaderErrorUtils {


    private LoaderErrorUtils() {
        // do not instantiate
    }

    private static ErrorKind getErrorKind(final int httpCode) {

        final ErrorKind retVal;
        if (httpCode == 404) {
            AppLog.w("404 Error!");
            retVal = ErrorKind.NOT_FOUND;
        } else if (httpCode >= 400 && httpCode <= 499) {
            retVal = ErrorKind.AUTHENTICATION;
        } else if (!(httpCode >= 500 && httpCode <= 599)) {
            retVal = ErrorKind.INTERNAL_SERVER_ERROR;
        } else {
            retVal = ErrorKind.REQUEST_FAILED;
        }

        return retVal;
    }


    public static Error getError(final Reason reason, final int httpStatus) {
        final ErrorKind errorKind = reason == Reason.UNKNOWN ? getErrorKind(httpStatus) : getErrorKind(reason);
        return new Error(errorKind, httpStatus);
    }

    private static ErrorKind getErrorKind(final Reason reason) {

        final ErrorKind retVal;
        switch (reason) {

            case DESERIALIZATION:
                retVal = ErrorKind.DESERIALIZATION_ERROR;
                break;
            case NETWORK_ISSUE:
                retVal = ErrorKind.COMMUNICATION;
                break;
            case TIMEOUT:
                retVal = ErrorKind.IO_EXCEPTION;
                break;
            case UNKNOWN:
                retVal = ErrorKind.UNEXPECTED;
                break;
            default:
                retVal = ErrorKind.UNEXPECTED;
        }

        return retVal;
    }
}