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
@Schema(description = "이벤트 수정 요청")
@ToString
@FieldNameConstants
public class EventUpdateRequestDto {

	@Schema(description = "이벤트 제목", example = "커넥트 이벤트")
	private final String title;

	@Schema(description = "이벤트 설명", example = "커넥트 이벤트 설명")
	private final String description;

	@Schema(description = "이벤트 시작 시간", example = "2021-08-01T10:00:00Z")
	private final LocalDateTime startedAt;

	@Schema(description = "이벤트 종료 시간", example = "2021-08-01T12:00:00Z")
	private final LocalDateTime endedAt;

	@Schema(description = "이벤트 위치 ID", example = "1")
	private final Long locationId;
}
