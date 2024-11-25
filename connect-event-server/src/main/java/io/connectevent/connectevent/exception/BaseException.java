package io.connectevent.connectevent.exception;


import io.connectevent.connectevent.exception.status.ErrorReason;

public interface BaseException {

	ErrorReason getErrorReason();
}

