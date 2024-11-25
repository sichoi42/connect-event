package io.connectevent.connectevent.exception.annotation;

import io.connectevent.connectevent.exception.status.AuthExceptionStatus;
import io.connectevent.connectevent.exception.status.EventExceptionStatus;
import io.connectevent.connectevent.exception.status.FeedbackExceptionStatus;
import io.connectevent.connectevent.exception.status.ParticipantExceptionStatus;
import io.connectevent.connectevent.exception.status.TagExceptionStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * // @formatter:off
 * Swagger 에러코드 예시를 명세하기 위한 커스텀 어노테이션입니다.
 * 해당 컨트롤러 메소드 위에 작성합니다.
 * 해당 API에서 발생할 수 있는 예외의 종류를 도메인 별로 명세합니다.
 * ex. 어떤 API에서 AuthExceptionStatus.UNAUTHORIZED, MemberExceptionStatus.NOT_FOUND_MEMBER
 * 예외가 발생할 수 있다면
 *
 *        @ApiErrorCodeExample(
 *            authExceptionStatuses = {
 * 					AuthExceptionStatus.UNAUTHORIZED,
 *            },
 *            memberExceptionStatuses = {
 *                  MemberExceptionStatus.NOT_FOUND_MEMBER,
 *            }
 *        )
 * 형태로 작성합니다.
 * // @formatter:on
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {

	AuthExceptionStatus[] authExceptionStatuses() default {};

	EventExceptionStatus[] eventExceptionStatuses() default {};

	TagExceptionStatus[] tagExceptionStatuses() default {};

	ParticipantExceptionStatus[] participantExceptionStatuses() default {};

	FeedbackExceptionStatus[] feedbackExceptionStatuses() default {};
}
