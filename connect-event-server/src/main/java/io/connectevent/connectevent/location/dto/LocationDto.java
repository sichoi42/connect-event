package io.connectevent.connectevent.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "장소 정보")
@ToString
@FieldNameConstants
public class LocationDto {

	@Schema(description = "장소 ID")
	private final Long locationId;

	@Schema(description = "장소 이름")
	private final String name;

	@Schema(description = "장소 수용 인원")
	private final Integer capacity;

	@Schema(description = "장소 주소")
	private final String address;
}
