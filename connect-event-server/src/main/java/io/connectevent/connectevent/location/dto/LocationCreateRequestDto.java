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
@Schema(description = "로그인 요청 DTO")
@ToString
@FieldNameConstants
public class LocationCreateRequestDto {

	@Schema(description = "장소 이름", example = "커넥트이벤트")
	private final String name;

	@Schema(description = "장소 수용 인원", example = "100")
	private final int capacity;

	@Schema(description = "장소 주소", example = "서울특별시 강남구 테헤란로 427")
	private final String address;
}
