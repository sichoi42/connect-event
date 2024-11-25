package io.connectevent.connectevent.exception;

import io.connectevent.connectevent.exception.status.ErrorReason;
import io.connectevent.connectevent.exception.status.ErrorReason.ErrorReasonBuilder;
import io.connectevent.connectevent.exception.status.ExceptionStatus;
import io.connectevent.connectevent.exception.status.ValidationExceptionStatusResolver;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController extends ResponseEntityExceptionHandler {

	private static final String DEFAULT_SPRING_MVC_ERROR_MESSAGE_VALUE = "Spring MVC 에서 예기치 않은 오류가 발생했어요.🥲";
	private static final String DEFAULT_ERROR_MESSAGE_VALUE = "Connect Event 서버에서 예기치 않은 오류가 발생했어요.🥲";

	private final ValidationExceptionStatusResolver validationExceptionStatusResolver;

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<?> controllerExceptionHandler(ControllerException e) {
		log.info("[ControllerException] {}", e.getErrorReason());
		if (log.isDebugEnabled()) {
			log.debug("Exception stack trace: ", e);
		}
		return ResponseEntity
				.status(e.getErrorReason().getStatusCode())
				.body(e.getErrorReason());
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> serviceExceptionHandler(ServiceException e) {
		log.info("[ServiceException] {}", e.getErrorReason());
		if (log.isDebugEnabled()) {
			log.debug("Exception stack trace: ", e);
		}
		return ResponseEntity
				.status(e.getErrorReason().getStatusCode())
				.body(e.getErrorReason());
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<?> domainExceptionHandler(DomainException e) {
		log.warn("[DomainException] {}", e.getErrorReason());
		if (log.isDebugEnabled()) {
			log.debug("Exception stack trace: ", e);
		}
		return ResponseEntity
				.status(e.getErrorReason().getStatusCode())
				.body(e.getErrorReason());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		log.warn("[AccessDeniedException] {}", e.getMessage());
		ErrorReason errorReason = ErrorReason.builder()
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.code("SECURITY")
				.message(e.getMessage())
				.build();
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(errorReason);
	}

	private ResponseEntity<Object> handleValidationExceptions(
			BindException e) {
		BindingResult result = e.getBindingResult();
		// 에러 목록에서 첫 번째 유효한 ExceptionStatus를 찾는다
		ExceptionStatus status = result.getAllErrors().stream()
				.map(error -> validationExceptionStatusResolver.findByErrorCode(error.getDefaultMessage()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("유효한 ExceptionStatus를 찾을 수 없습니다."));

		log.warn("[BindException] {}", status.getErrorReason().getMessage());

		return ResponseEntity
				.status(status.getErrorReason().getStatusCode())
				.body(status.getErrorReason());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception e, Object body,
			org.springframework.http.HttpHeaders headers,
			HttpStatus status,
			org.springframework.web.context.request.WebRequest request) {
		if (e instanceof BindException) {
			return handleValidationExceptions((BindException) e);
		}
		String requestUri = request.getContextPath();
		ErrorReasonBuilder errorReasonBuilder = ErrorReason.builder()
				.statusCode(status.value())
				.code("SPRINGMVC")
				.message(e.getLocalizedMessage());
		ErrorReason errorReason;

		if (status.is5xxServerError()) {
			errorReasonBuilder.message(DEFAULT_SPRING_MVC_ERROR_MESSAGE_VALUE);
			log.error("[SpringMVCServerError] {}: {} at {}",
					status.getReasonPhrase(),
					e.getMessage(),
					requestUri);
			errorReason = errorReasonBuilder.build();
			log.error("Exception stack trace: ", e);
		} else {
			log.warn("[SpringMVCClientError] {}: {} at {}",
					status.getReasonPhrase(),
					e.getMessage(),
					requestUri);
			errorReason = errorReasonBuilder.build();
		}
		return ResponseEntity
				.status(status)
				.headers(headers)
				.body(errorReason);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleInternalServerErrorException(Exception e,
			HttpServletRequest request) {
		log.error("[UncheckedException] {} for request URL: {}", e.getMessage(),
				request.getRequestURL());
		log.error("Exception stack trace: ", e);

		ErrorReason errorReason = ErrorReason.builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.code("UNKNOWN")
				.message(DEFAULT_ERROR_MESSAGE_VALUE)
				.build();
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(errorReason);
	}
}
