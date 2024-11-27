package io.connectevent.connectevent.exception.status;

import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum FeedbackExceptionStatus implements ExceptionStatus {

	FEEDBACK_NOT_FOUND(404, "FEEDBACK001", "존재하지 않는 피드백입니다."),
	FEEDBACK_ALREADY_EXISTS(409, "FEEDBACK002", "이미 존재하는 피드백입니다."),
	FEEDBACK_NOT_OWNER(403, "FEEDBACK003", "피드백 작성자가 아닙니다.");

	private final int statusCode;
	private final String code;
	private final String message;

	@Override
	public ErrorReason getErrorReason() {
		return ErrorReason.builder()
				.statusCode(statusCode)
				.code(code)
				.message(message)
				.build();
	}

	@Override
	public ControllerException toControllerException() {
		return new ControllerException(this);
	}

	@Override
	public ServiceException toServiceException() {
		return new ServiceException(this);
	}

	@Override
	public DomainException toDomainException() {
		return new DomainException(this);
	}
}
