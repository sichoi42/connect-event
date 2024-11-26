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
@Schema(description = "태그 정보")
@ToString
@FieldNameConstants
public class TagDto {

	@Schema(description = "태그 ID", example = "1")
	private Long tagId;

	@Schema(description = "태그 이름", example = "커넥트")
	private String tagName;
}
