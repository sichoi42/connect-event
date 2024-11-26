package io.connectevent.connectevent.event.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
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
public class EventDetailDto {

	@Schema(description = "이벤트 ID", example = "1")
	private Long eventId;

	@Schema(description = "이벤트 제목", example = "커넥트 이벤트")
	private String title;

	@Schema(description = "이벤트 설명", example = "커넥트 이벤트 설명")
	private String description;

	@Schema(description = "이벤트 시작 시간", example = "2021-08-01T00:00:00Z")
	private LocalDateTime startedAt;

	@Schema(description = "이벤트 종료 시간", example = "2021-08-01T00:00:00Z")
	private LocalDateTime endedAt;

	@Schema(description = "이벤트 위치 정보", implementation = EventLocationDto.class)
	EventLocationDto location;

	@ArraySchema(schema = @Schema(implementation = EventTagDto.class))
	private final List<EventTagDto> eventTags;
}
