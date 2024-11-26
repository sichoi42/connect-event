package io.connectevent.connectevent.participant.dto;

import io.connectevent.connectevent.participant.domain.ParticipantRole;
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
public class ParticipantDto {

	@Schema(description = "참가자 ID", example = "1")
	private final Long participantId;

	@Schema(description = "이름", example = "홍길동")
	private final String name;

	@Schema(description = "이메일", example = "example@gmail.com")
	private final String email;

	@Schema(description = "등록일", example = "2023-01-01T00:00:00Z")
	private final LocalDateTime registeredAt;

	@Schema(description = "역할", example = "MASTER")
	private final ParticipantRole role;
}
