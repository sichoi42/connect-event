package io.connectevent.connectevent.event.dto;

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
@Schema(description = "이벤트 미리보기 정보")
@ToString
@FieldNameConstants
public class EventPreviewDto {

	@Schema(description = "이벤트 ID", example = "1")
	private final Long eventId;

	@Schema(description = "제목", example = "Connect Event")
	private final String title;

	@Schema(description = "미리보기 내용", example = "This is the story of my early life...")
	private final String descriptionPreview;

	@Schema(description = "시작일", example = "2023-01-01T00:00:00Z")
	private final LocalDateTime startedAt;

	@Schema(description = "종료일", example = "2023-01-02T00:00:00Z")
	private final LocalDateTime endedAt;
}
