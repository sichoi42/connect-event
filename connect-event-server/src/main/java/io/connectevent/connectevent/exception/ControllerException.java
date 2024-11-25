package io.connectevent.connectevent.exception;

import io.connectevent.connectevent.exception.status.ErrorReason;
import io.connectevent.connectevent.exception.status.ExceptionStatus;
import org.springframework.http.HttpStatus;

public class ControllerException extends RuntimeException implements BaseException {

	private final ErrorReason errorReason;

	/**
	 * @param status exception에 대한 정보에 대한 enum
	 */
	public ControllerException(ExceptionStatus status) {
		this.errorReason = status.getErrorReason();
	}

	public ControllerException(HttpStatus status, String message) {
		this.errorReason = new ErrorReason(status.value(), message, status.getReasonPhrase());
	}

	@Override
	public ErrorReason getErrorReason() {
		return this.errorReason;
	}
}
