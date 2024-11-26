package io.connectevent.connectevent.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "이벤트 위치 정보")
@ToString
@FieldNameConstants
public class EventLocationDto {

	@Schema(description = "이벤트 위치 ID", example = "1")
	private Long locationId;

	@Schema(description = "이벤트 위치 이름", example = "커넥트 이벤트장")
	private String name;

	@Schema(description = "수용 인원", example = "100")
	private Integer capacity;

	@Schema(description = "주소", example = "서울시 강남구")
	private String address;
}
