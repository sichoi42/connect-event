package io.connectevent.connectevent.mapper;

import io.connectevent.connectevent.participant.domain.Participant;
import io.connectevent.connectevent.participant.dto.ParticipantDto;
import io.connectevent.connectevent.participant.dto.ParticipantListResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

	@Mapping(source = "participant.id", target = "participantId")
	ParticipantDto toParticipantDto(Participant participant, String name, String email);

	default ParticipantListResponseDto toParticipantListResponseDto(List<ParticipantDto> participantDtos) {
		return ParticipantListResponseDto.builder()
				.results(participantDtos)
				.build();
	}
}
