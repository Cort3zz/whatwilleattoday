package com.ppaper.norbi.whatwillweeattoday.exception;

import java.sql.SQLException;

public class WWWETException extends Exception {

    public WWWETException(String message) {
        super(message);
    }

    public WWWETException(String message, Throwable cause) {
        super(message, cause);
    }
}
