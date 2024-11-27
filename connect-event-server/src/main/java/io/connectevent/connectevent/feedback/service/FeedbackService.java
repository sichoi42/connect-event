package io.connectevent.connectevent.feedback.service;

import io.connectevent.connectevent.event.domain.Event;
import io.connectevent.connectevent.event.repository.EventRepository;
import io.connectevent.connectevent.exception.status.EventExceptionStatus;
import io.connectevent.connectevent.exception.status.FeedbackExceptionStatus;
import io.connectevent.connectevent.exception.status.ParticipantExceptionStatus;
import io.connectevent.connectevent.feedback.domain.Feedback;
import io.connectevent.connectevent.feedback.dto.FeedbackCreateRequestDto;
import io.connectevent.connectevent.feedback.dto.FeedbackDto;
import io.connectevent.connectevent.feedback.dto.FeedbackListResponseDto;
import io.connectevent.connectevent.feedback.dto.FeedbackUpdateRequestDto;
import io.connectevent.connectevent.feedback.repository.FeedbackRepository;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.mapper.FeedbackMapper;
import io.connectevent.connectevent.member.domain.Member;
import io.connectevent.connectevent.participant.domain.Participant;
import io.connectevent.connectevent.participant.repository.ParticipantRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Logging
public class FeedbackService {

	private final EventRepository eventRepository;
	private final FeedbackRepository feedbackRepository;
	private final FeedbackMapper feedbackMapper;
	private final ParticipantRepository participantRepository;

	public FeedbackListResponseDto getFeedbacks(Long eventId, Pageable pageable) {

		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Page<Feedback> feedbacks = feedbackRepository.findAllByEventId(eventId, pageable);
		List<FeedbackDto> feedbackDtos = feedbacks.stream().map(feedback -> {
			Member member = feedback.getParticipant().getMember();
			return feedbackMapper.toFeedbackDto(feedback, member);
		}).toList();

		return feedbackMapper.toFeedbackListResponseDto(
				feedbackDtos,
				pageable.getPageNumber(),
				(int) feedbacks.getTotalElements(),
				feedbacks.getTotalPages(),
				feedbacks.hasNext(),
				feedbacks.hasPrevious()
		);
	}

	public void createFeedback(Long eventId, Long memberId, FeedbackCreateRequestDto requestDto) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Participant participant = participantRepository.findByMemberIdAndEventId(memberId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);

		Feedback feedback = Feedback.of(event, participant, requestDto.getRating(), requestDto.getComment(),
				LocalDateTime.now());

		feedbackRepository.save(feedback);
	}

	public void updateFeedback(Long eventId, Long memberId, Long feedbackId, FeedbackUpdateRequestDto requestDto) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Participant participant = participantRepository.findByMemberIdAndEventId(memberId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);

		Feedback feedback = feedbackRepository.findById(feedbackId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		if (!feedback.getParticipant().equals(participant)) {
			throw FeedbackExceptionStatus.FEEDBACK_NOT_OWNER.toServiceException();
		}

		feedback.update(requestDto.getRating(), requestDto.getComment());
		feedbackRepository.save(feedback);
	}

	public void deleteFeedback(Long eventId, Long memberId, Long feedbackId) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		Participant participant = participantRepository.findByMemberIdAndEventId(memberId, eventId)
				.orElseThrow(ParticipantExceptionStatus.PARTICIPANT_NOT_FOUND::toServiceException);

		Feedback feedback = feedbackRepository.findById(feedbackId)
				.orElseThrow(EventExceptionStatus.EVENT_NOT_FOUND::toServiceException);

		if (!feedback.getParticipant().equals(participant)) {
			throw FeedbackExceptionStatus.FEEDBACK_NOT_OWNER.toServiceException();
		}

		feedbackRepository.delete(feedback);
	}
}
