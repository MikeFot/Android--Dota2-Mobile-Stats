package com.michaelfotiadis.dota2viewer.data.persistence.error;

import android.content.Context;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;

/**
 *
 */
public final class UiDataLoadErrorFactory {


    private UiDataLoadErrorFactory() {
    }

    public static UiDataLoadError createError(final Context context,
                                              final Error error) {
        //noinspection IfMayBeConditional
        if (error == null) {
            return createServerError(context);
        } else if (error.getKind().equals(ErrorKind.NO_NETWORK)) {
            return createDeviceOfflineError(context);
        } else if (error.getKind().equals(ErrorKind.AUTHENTICATION)) {
            return createAuthenticationError(context);
        } else {
            final UiDataLoadError.ErrorKind kind = translate(error.getKind());
            return new UiDataLoadError(
                    getMessageFromKind(context, kind),
                    kind,
                    isRecoverable(kind));
        }
    }

    private static UiDataLoadError.ErrorKind translate(final ErrorKind kind) {
        final UiDataLoadError.ErrorKind result;

        switch (kind) {
            case COMMUNICATION:
                result = UiDataLoadError.ErrorKind.GENERIC;
                break;
            case INTERNAL_SERVER_ERROR:
                result = UiDataLoadError.ErrorKind.SERVER_ERROR;
                break;
            case UNEXPECTED:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
                break;
            case REQUEST_FAILED:
                result = UiDataLoadError.ErrorKind.NO_DATA;
                break;
            case NO_NETWORK:
                result = UiDataLoadError.ErrorKind.NO_NETWORK;
                break;
            case ERROR_RETRIEVING_FROM_CACHE:
                result = UiDataLoadError.ErrorKind.NO_DATA;
                break;
            case ERROR_PERSISTING:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
                break;
            case IO_EXCEPTION:
                result = UiDataLoadError.ErrorKind.NO_DATA;
                break;
            case DESERIALIZATION_ERROR:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
                break;
            case INVALID_REQUEST_PARAMETERS:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
                break;
            case INVALID_CONTENT:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
                break;
            case NO_CONTENT_RETURNED:
                result = UiDataLoadError.ErrorKind.NO_DATA;
                break;
            case NOT_FOUND:
                result = UiDataLoadError.ErrorKind.NOT_FOUND;
                break;
            case AUTHENTICATION:
                result = UiDataLoadError.ErrorKind.AUTHENTICATION;
                break;
            case NO_PERMISSION:
                result = UiDataLoadError.ErrorKind.PERMISSION_DENIED;
                break;
            default:
                result = UiDataLoadError.ErrorKind.UNKNOWN;
        }
        return result;
    }

    public static UiDataLoadError createAuthenticationError(final Context context) {
        return new UiDataLoadError(
                context.getString(R.string.error_authentication),
                UiDataLoadError.ErrorKind.AUTHENTICATION,
                true);
    }

    public static UiDataLoadError createServerError(final Context context) {
        return new UiDataLoadError(
                context.getString(R.string.error_failed_to_retrieve_data),
                UiDataLoadError.ErrorKind.NO_DATA,
                true);
    }

    public static UiDataLoadError createDeviceOfflineError(final Context context) {
        return new UiDataLoadError(
                context.getString(R.string.error_network),
                UiDataLoadError.ErrorKind.NO_NETWORK,
                true);
    }

    public static UiDataLoadError createGenericError(final Context context) {
        return new UiDataLoadError(
                context.getString(R.string.error_server),
                UiDataLoadError.ErrorKind.UNKNOWN,
                true);
    }

    private static boolean isRecoverable(final UiDataLoadError.ErrorKind kind) {
        switch (kind) {
            case UNKNOWN:
                return false;
            case AUTHENTICATION:
                return true;
            case SERVER_ERROR:
                return true;
            case NO_NETWORK:
                return true;
            case NO_DATA:
                return true;
            default:
                return false;
        }
    }

    private static CharSequence getMessageFromKind(final Context context,
                                                   final UiDataLoadError.ErrorKind kind) {

        switch (kind) {
            case UNKNOWN:
                return context.getString(R.string.error_server);
            case NO_NETWORK:
                return context.getString(R.string.error_network);
            case NO_DATA:
                return context.getString(R.string.error_failed_to_retrieve_data);
            case AUTHENTICATION:
                return context.getString(R.string.error_authentication);
            case PERMISSION_DENIED:
                return context.getString(R.string.error_permission_denied);
            default:
                return context.getString(R.string.error_server);
        }

    }
}
