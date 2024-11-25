package io.connectevent.connectevent.exception.status;

import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum ParticipantExceptionStatus implements ExceptionStatus {

	PARTICIPANT_NOT_FOUND(404, "PARTICIPANT001", "존재하지 않는 참가자입니다."),
	PARTICIPANT_ALREADY_EXISTS(409, "PARTICIPANT002", "이미 존재하는 참가자입니다.")
	;

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
