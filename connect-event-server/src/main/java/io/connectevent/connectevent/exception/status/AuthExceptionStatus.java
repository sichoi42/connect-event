package io.connectevent.connectevent.exception.status;

import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum AuthExceptionStatus implements ExceptionStatus {
	INVALID_EMAIL_FORMAT(400, "AUTH001", "이메일 형식이 올바른지 다시 확인해주세요. 옳은 예: john.doe@example.com"),
	EMAIL_TOO_LONG(400, "AUTH002", "이메일은 비어있을 수 없으며, 최대 64자까지 입력할 수 있습니다."),
	PASSWORD_FORMAT_ERROR(400, "AUTH003",
			"비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8자이상, 최대 64자이하까지 입력할 수 있습니다."),
	EMAIL_OR_PASSWORD_INCORRECT(401, "AUTH006", "이메일 또는 비밀번호가 일치하지 않습니다."),
	MEMBER_NOT_FOUND(404, "AUTH008", "존재하지 않는 회원입니다."),
	MEMBER_ALREADY_EXISTS(409, "AUTH004", "이미 존재하는 회원입니다."),
	MEMBER_ALREADY_WITHDRAWN(409, "AUTH009", "이미 탈퇴한 회원입니다.");

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
