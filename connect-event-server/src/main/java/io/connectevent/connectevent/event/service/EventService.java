package io.connectevent.connectevent.event.service;

import io.connectevent.connectevent.event.domain.Event;
import io.connectevent.connectevent.event.domain.EventTag;
import io.connectevent.connectevent.event.dto.EventCreateRequestDto;
import io.connectevent.connectevent.event.dto.EventDetailDto;
import io.connectevent.connectevent.event.dto.EventListResponseDto;
import io.connectevent.connectevent.event.dto.EventPreviewDto;
import io.connectevent.connectevent.event.dto.EventTagDto;
import io.connectevent.connectevent.event.dto.EventUpdateRequestDto;
import io.connectevent.connectevent.event.dto.TagDto;
import io.connectevent.connectevent.event.repository.EventRepository;
import io.connectevent.connectevent.event.repository.EventTagRepository;
import io.connectevent.connectevent.exception.status.AuthExceptionStatus;
import io.connectevent.connectevent.exception.status.EventExceptionStatus;
import io.connectevent.connectevent.exception.status.LocationExceptionStatus;
import io.connectevent.connectevent.location.domain.Location;
import io.connectevent.connectevent.location.dto.LocationDto;
import io.connectevent.connectevent.location.repository.LocationRepository;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.mapper.EventMapper;
import io.connectevent.connectevent.mapper.LocationMapper;
import io.connectevent.connectevent.member.domain.Member;
import io.connectevent.connectevent.member.repository.MemberRepository;
import io.connectevent.connectevent.participant.domain.Participant;
import io.connectevent.connectevent.participant.domain.ParticipantRole;
import io.connectevent.connectevent.participant.repository.ParticipantRepository;
import io.connectevent.connectevent.tag.domain.Tag;
import io.connectevent.connectevent.tag.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class EventService {

	private final EventRepository eventRepository;
	private final EventTagRepository eventTagRepository;
	private final LocationRepository locationRepository;
	private final ParticipantRepository participantRepository;
	private final EventMapper eventMapper;
	private final LocationMapper locationMapper;
	private final TagRepository tagRepository;
	private final MemberRepository memberRepository;

	public EventListResponseDto getEvents(Pageable pageable) {
		Page<Event> events = eventRepository.findAll(pageable);
		List<EventPreviewDto> eventPreviewDtos = events.stream()
				.map(eventMapper::toEventPreviewDto)
				.toList();

		return eventMapper.toEventListResponseDto(
				eventPreviewDtos,
				pageable.getPageNumber(),
				(int) events.getTotalElements(),
				events.getTotalPages(),
				events.hasNext(),
				events.hasPrevious()
		);
	}

	public EventDetailDto getEventDetail(Long eventId) {
		Event event = eventRepository.findByIdWithLocationAndTags(eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_FOUND::toServiceException
		);
		LocationDto locationDto = locationMapper.toLocationDto(event.getLocation());
		List<EventTag> eventTags = event.getEventTags();
		List<EventTagDto> eventTagDtos = eventTags.stream()
				.map(eventMapper::toEventTagDto)
				.toList();
		return eventMapper.toEventDetailDto(event, locationDto, eventTagDtos);
	}

	public List<TagDto> getTags(Pageable pageable, String query) {
		Page<Tag> tags = tagRepository.findAllByNameContaining(query, pageable);
		return tags.stream()
				.map(eventMapper::toTagDto)
				.toList();
	}

	public TagDto createTag(String tagName) {
		if (tagRepository.findByName(tagName).isPresent()) {
			throw EventExceptionStatus.TAG_ALREADY_EXISTS.toServiceException();
		}

		Tag tag = Tag.of(tagName);
		Tag savedTag = tagRepository.save(tag);
		return eventMapper.toTagDto(savedTag);
	}

	public void addTag(Long eventId, Long tagId) {
		Event event = eventRepository.findById(eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_FOUND::toServiceException
		);
		Tag tag = tagRepository.findById(tagId).orElseThrow(
				EventExceptionStatus.TAG_NOT_FOUND::toServiceException
		);
		EventTag eventTag = EventTag.of(event, tag);
		eventTagRepository.save(eventTag);
	}

	public void deleteTag(Long eventId, Long eventTagId) {
		Event event = eventRepository.findById(eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_FOUND::toServiceException
		);
		EventTag eventTag = eventTagRepository.findById(eventTagId).orElseThrow(
				EventExceptionStatus.EVENT_TAG_NOT_FOUND::toServiceException
		);
		if (!eventTag.getEvent().equals(event)) {
			throw EventExceptionStatus.EVENT_TAG_NOT_FOUND.toServiceException();
		}
		eventTagRepository.delete(eventTag);
	}

	public EventDetailDto createEvent(Long memberId, EventCreateRequestDto requestDto) {
		Location location = locationRepository.findById(requestDto.getLocationId()).orElseThrow(
				LocationExceptionStatus.LOCATION_NOT_FOUND::toServiceException
		);

		Member member = memberRepository.findById(memberId).orElseThrow(
				AuthExceptionStatus.MEMBER_NOT_FOUND::toServiceException
		);

		Event event = Event.of(
				member,
				requestDto.getTitle(),
				requestDto.getDescription(),
				requestDto.getStartedAt(),
				requestDto.getEndedAt(),
				location
		);

		List<EventTag> eventTags = new ArrayList<>();
		for (Long tagId : requestDto.getTags()) {
			Tag tag = tagRepository.findById(tagId).orElseThrow(
					EventExceptionStatus.TAG_NOT_FOUND::toServiceException
			);
			EventTag eventTag = EventTag.of(event, tag);
			eventTags.add(eventTag);
		}
		event.setEventTags(eventTags);
		Event savedEvent = eventRepository.save(event);

		eventTagRepository.saveAll(eventTags);

		Participant participant = Participant.of(member, savedEvent, LocalDateTime.now(), ParticipantRole.MASTER);
		participantRepository.save(participant);

		LocationDto locationDto = locationMapper.toLocationDto(location);

		List<EventTagDto> eventTagDtos = eventTags.stream()
				.map(eventMapper::toEventTagDto)
				.toList();
		return eventMapper.toEventDetailDto(savedEvent, locationDto, eventTagDtos);
	}

	public EventDetailDto updateEvent(Long memberId, Long eventId, EventUpdateRequestDto requestDto) {
		Event event = eventRepository.findById(eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_FOUND::toServiceException
		);
		Participant participant = participantRepository.findByMemberIdAndEventId(memberId, eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_OWNER::toServiceException
		);
		if (!participant.getRole().equals(ParticipantRole.MASTER)) {
			throw EventExceptionStatus.EVENT_NOT_OWNER.toServiceException();
		}

		Location location = locationRepository.findById(requestDto.getLocationId()).orElseThrow(
				LocationExceptionStatus.LOCATION_NOT_FOUND::toServiceException
		);
		LocationDto locationDto = locationMapper.toLocationDto(location);

		event.setTitle(requestDto.getTitle());
		event.setDescription(requestDto.getDescription());
		event.setStartedAt(requestDto.getStartedAt());
		event.setEndedAt(requestDto.getEndedAt());
		event.setLocation(location);

		List<EventTag> eventTags = eventTagRepository.findByEventId(eventId);

		List<EventTagDto> eventTagDtos = eventTags.stream()
				.map(eventMapper::toEventTagDto)
				.toList();
		return eventMapper.toEventDetailDto(event, locationDto, eventTagDtos);
	}

	public void deleteEvent(Long memberId, Long eventId) {
		Event event = eventRepository.findById(eventId).orElseThrow(
				EventExceptionStatus.EVENT_NOT_FOUND::toServiceException
		);
		List<Participant> participants = participantRepository.findByEventId(eventId);
		if (participants.size() > 1) {
			throw EventExceptionStatus.PARTICIPANT_EXISTS.toServiceException();
		}
		Participant participant = participants.get(0);
		if (!participant.getMember().getId().equals(memberId)) {
			throw EventExceptionStatus.EVENT_NOT_OWNER.toServiceException();
		}
		eventRepository.delete(event);
	}
}
