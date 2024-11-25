package io.connectevent.connectevent.exception.status;

import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum LocationExceptionStatus implements ExceptionStatus {

	LOCATION_NOT_FOUND(404, "LOCATION001", "존재하지 않는 장소입니다."),
	LOCATION_ALREADY_EXISTS(409, "LOCATION002", "이미 존재하는 장소입니다."),
	LOCATION_NAME_LENGTH_EXCEEDED(400, "LOCATION003", "장소 이름은 100자 이하로 입력해주세요."),
	LOCATION_ADDRESS_LENGTH_EXCEEDED(400, "LOCATION004", "장소 주소는 100자 이하로 입력해주세요."),
	LOCATION_DESCRIPTION_LENGTH_EXCEEDED(400, "LOCATION005", "장소 설명은 500자 이하로 입력해주세요.");

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
