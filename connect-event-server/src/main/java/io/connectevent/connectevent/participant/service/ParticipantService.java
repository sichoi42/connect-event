package io.connectevent.connectevent.participant.service;

import io.connectevent.connectevent.event.domain.Event;
import io.connectevent.connectevent.event.repository.EventRepository;
import io.connectevent.connectevent.exception.status.AuthExceptionStatus;
import io.connectevent.connectevent.exception.status.EventExceptionStatus;
import io.connectevent.connectevent.exception.status.ParticipantExceptionStatus;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.mapper.ParticipantMapper;
import io.connectevent.connectevent.member.domain.Member;
import io.connectevent.connectevent.member.repository.MemberRepository;
import io.connectevent.connectevent.participant.domain.Participant;
import io.connectevent.connectevent.participant.domain.ParticipantRole;
import io.connectevent.connectevent.participant.dto.ParticipantDto;
import io.connectevent.connectevent.participant.dto.ParticipantListResponseDto;
import io.connectevent.connectevent.participant.repository.ParticipantRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Logging
public class ParticipantService {

	private final ParticipantRepository participantRepository;
	private final EventRepository eventRepository;
	private final ParticipantMapper participantMapper;
	private final MemberRepository memberRepository;

	public ParticipantListResponseDto getParticipants(Long eventId) {

		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		List<Participant> participants = participantRepository.findByEventIdWithMember(eventId);
		List<ParticipantDto> participantDtos = participants.stream()
				.map(participant -> {
					Member member = participant.getMember();
					return participantMapper.toParticipantDto(participant, member.getName(), member.getEmail());
				})
				.toList();

		return participantMapper.toParticipantListResponseDto(participantDtos);
	}

	public void requestEvent(Long eventId, Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(AuthExceptionStatus.MEMBER_NOT_FOUND::toServiceException);

		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		participantRepository.findByMemberIdAndEventId(memberId, eventId)
				.ifPresent(participant -> {
					throw ParticipantExceptionStatus.PARTICIPANT_ALREADY_EXISTS.toServiceException();
				});

		Participant participant = Participant.of(member, event, null, ParticipantRole.MEMBER);
		participantRepository.save(participant);
	}

	public void acceptEvent(Long requestMemberId, Long eventId, Long participantId) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Participant requestParticipant = participantRepository.findByMemberIdAndEventId(requestMemberId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);
		if (!requestParticipant.getRole().equals(ParticipantRole.MASTER)) {
			throw EventExceptionStatus.EVENT_NOT_OWNER.toServiceException();
		}

		Participant participant = participantRepository.findByMemberIdAndEventId(participantId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);

		participant.setRegisteredAt(LocalDateTime.now());
	}

	public void cancelEvent(Long memberId, Long eventId) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Participant participant = participantRepository.findByMemberIdAndEventId(memberId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);

		if (participant.getRole().equals(ParticipantRole.MASTER)) {
			throw ParticipantExceptionStatus.PARTICIPANT_NOT_MEMBER.toServiceException();
		}

		participantRepository.delete(participant);
	}
}
