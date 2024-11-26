package io.connectevent.connectevent.event.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "이벤트 목록 조회 응답")
@ToString
@FieldNameConstants
public class EventListResponseDto {

	@ArraySchema(schema = @Schema(implementation = EventPreviewDto.class))
	private final List<EventPreviewDto> results;

	@Schema(description = "현재 페이지", example = "1")
	private final int currentPage;

	@Schema(description = "전체 대화 수", example = "2")
	private final int totalElements;

	@Schema(description = "전체 페이지 수", example = "1")
	private final int totalPages;

	@Schema(description = "다음 페이지 존재 여부", example = "false")
	private final boolean hasNextPage;

	@Schema(description = "이전 페이지 존재 여부", example = "false")
	private final boolean hasPreviousPage;
}
