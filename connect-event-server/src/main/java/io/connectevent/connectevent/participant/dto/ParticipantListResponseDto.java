package io.connectevent.connectevent.participant.dto;

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
@Schema(description = "참가자 목록 조회 응답")
@ToString
@FieldNameConstants
public class ParticipantListResponseDto {

	@ArraySchema(schema = @Schema(implementation = ParticipantDto.class))
	private final List<ParticipantDto> results;
}