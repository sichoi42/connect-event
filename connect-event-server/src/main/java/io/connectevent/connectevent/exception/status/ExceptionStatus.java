package io.connectevent.connectevent.exception.status;


import io.connectevent.connectevent.exception.ControllerException;
import io.connectevent.connectevent.exception.DomainException;
import io.connectevent.connectevent.exception.ServiceException;

public interface ExceptionStatus {

	ErrorReason getErrorReason();

	ControllerException toControllerException();

	ServiceException toServiceException();

	DomainException toDomainException();
}
