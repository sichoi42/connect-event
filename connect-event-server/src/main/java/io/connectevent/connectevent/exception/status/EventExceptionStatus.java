package io.connectevent.connectevent.exception.status;

import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum EventExceptionStatus implements ExceptionStatus {

	EVENT_NOT_FOUND(404, "EVENT001", "존재하지 않는 이벤트입니다."),
	EVENT_ALREADY_EXISTS(409, "EVENT002", "이미 존재하는 이벤트입니다."),
	TAG_ALREADY_EXISTS(409, "EVENT003", "이미 존재하는 태그입니다."),
	TAG_NOT_FOUND(404, "EVENT004", "존재하지 않는 태그입니다."),
	EVENT_TAG_NOT_FOUND(404, "EVENT005", "존재하지 않는 이벤트 태그입니다."),
	EVENT_NOT_OWNER(403, "EVENT006", "이벤트 주최자만 가능합니다."),
	PARTICIPANT_EXISTS(409, "EVENT007", "이미 참가한 이벤트입니다."),
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
