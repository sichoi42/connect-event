package io.connectevent.connectevent.feedback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "피드백 정보")
@ToString
@FieldNameConstants
public class FeedbackDto {

	@Schema(description = "피드백 ID", example = "1")
	private final Long feedbackId;

	@Schema(description = "회원 ID", example = "1")
	private final Long memberId;

	@Schema(description = "회원 이름", example = "John Doe")
	private final String memberName;

	@Schema(description = "회원 이메일", example = "example@gmail.com")
	private final String memberEmail;

	@Schema(description = "코멘트", example = "This is a great event!")
	private final String comment;

	@Schema(description = "평점", example = "5")
	private final int rating;

	@Schema(description = "생성일시", example = "2023-01-01T00:00:00Z")
	private final LocalDateTime createdAt;
}
