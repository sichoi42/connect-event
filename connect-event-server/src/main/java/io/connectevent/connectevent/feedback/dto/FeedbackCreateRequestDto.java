package io.connectevent.connectevent.feedback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "이벤트 생성 요청")
@ToString
@FieldNameConstants
public class FeedbackCreateRequestDto {

	@Schema(description = "코멘트", example = "This is a great event!")
	private final String comment;

	@Schema(description = "평점", example = "5")
	private final int rating;
}
