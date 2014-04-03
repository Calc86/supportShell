package com.net;

/**
 * Created by calc on 02.04.14.
 *
 */
public class NetException extends RuntimeException {
    public NetException() {
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }

    public NetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
