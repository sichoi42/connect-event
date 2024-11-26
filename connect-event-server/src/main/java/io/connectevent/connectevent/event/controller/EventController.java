package io.connectevent.connectevent.event.controller;

import io.connectevent.connectevent.auth.dto.MemberSessionDto;
import io.connectevent.connectevent.auth.jwt.LoginMemberInfo;
import io.connectevent.connectevent.event.dto.EventCreateRequestDto;
import io.connectevent.connectevent.event.dto.EventDetailDto;
import io.connectevent.connectevent.event.dto.EventListResponseDto;
import io.connectevent.connectevent.event.dto.EventUpdateRequestDto;
import io.connectevent.connectevent.event.dto.TagDto;
import io.connectevent.connectevent.event.service.EventService;
import io.connectevent.connectevent.exception.annotation.ApiErrorCodeExample;
import io.connectevent.connectevent.exception.status.EventExceptionStatus;
import io.connectevent.connectevent.exception.status.LocationExceptionStatus;
import io.connectevent.connectevent.log.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/events")
@Tag(name = "이벤트 (Event)", description = "이벤트 관련 API")
@Logging
public class EventController {

	private final EventService eventService;

	@Operation(summary = "이벤트 목록 조회", description = "이벤트 목록을 조회합니다. 페이지네이션을 지원합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public EventListResponseDto getEvents(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
			return eventService.getEvents(PageRequest.of(page, size));
	}

	@Operation(summary = "이벤트 상세 정보 조회", description = "이벤트 상세 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_NOT_FOUND
			}
	)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public EventDetailDto getEventDetail(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("eventId") @Parameter(description = "이벤트 ID") Long eventId
	) {
			return eventService.getEventDetail(eventId);
	}

	@Operation(summary = "태그 목록 조회", description = "태그 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/tags")
	@ResponseStatus(HttpStatus.OK)
	public List<TagDto> getTags(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "query", defaultValue = "") String query,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
			return eventService.getTags(PageRequest.of(0, size), query);
	}

	@Operation(summary = "신규 태그 생성", description = "신규 태그를 생성합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.TAG_ALREADY_EXISTS
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/tags/new")
	@ResponseStatus(HttpStatus.CREATED)
	public TagDto createTag(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "tagName") String tagName
	) {
			return eventService.createTag(tagName);
	}

	@Operation(summary = "이벤트에 태그 등록", description = "이벤트에 태그를 등록합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_NOT_FOUND,
					EventExceptionStatus.TAG_NOT_FOUND
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/{eventId}/tags/{tagId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void addTag(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("eventId") @Parameter(description = "이벤트 ID") Long eventId,
			@PathVariable("tagId") @Parameter(description = "태그 ID") Long tagId
	) {
			eventService.addTag(eventId, tagId);
	}

	@Operation(summary = "이벤트에서 태그를 삭제", description = "이벤트에서 태그를 삭제합니다. 주최자만 가능")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_NOT_FOUND,
					EventExceptionStatus.EVENT_TAG_NOT_FOUND,
					EventExceptionStatus.EVENT_NOT_OWNER
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/{eventId}/tags/{eventTagId}/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTag(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("eventId") @Parameter(description = "이벤트 ID") Long eventId,
			@PathVariable("eventTagId") @Parameter(description = "이벤트 태그 ID") Long eventTagId
	) {
			eventService.deleteTag(eventId, eventTagId);
	}

	@Operation(summary = "이벤트 개최", description = "이벤트를 개최합니다. 자기 자신을 주최자로 등록합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_ALREADY_EXISTS
			},
			locationExceptionStatuses = {
					LocationExceptionStatus.LOCATION_NOT_FOUND
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public EventDetailDto createEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@Valid @RequestBody EventCreateRequestDto requestDto
	) {
			return eventService.createEvent(memberSessionDto.getMemberId(), requestDto);
	}

	@Operation(summary = "이벤트 수정", description = "이벤트를 수정합니다. 주최자만 가능")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_NOT_FOUND,
					EventExceptionStatus.EVENT_NOT_OWNER
			},
			locationExceptionStatuses = {
					LocationExceptionStatus.LOCATION_NOT_FOUND
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{eventId}/update")
	@ResponseStatus(HttpStatus.OK)
	public EventDetailDto updateEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("eventId") @Parameter(description = "이벤트 ID") Long eventId,
			@Valid @RequestBody EventUpdateRequestDto requestDto
	) {
			return eventService.updateEvent(memberSessionDto.getMemberId(), eventId, requestDto);
	}

	@Operation(summary = "이벤트 삭제", description = "이벤트를 삭제합니다. 주최자만 가능")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content"),
	})
	@ApiErrorCodeExample(
			eventExceptionStatuses = {
					EventExceptionStatus.EVENT_NOT_FOUND,
					EventExceptionStatus.EVENT_NOT_OWNER,
					EventExceptionStatus.PARTICIPANT_EXISTS
			}
	)
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{eventId}/delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("eventId") @Parameter(description = "이벤트 ID") Long eventId
	) {
			eventService.deleteEvent(memberSessionDto.getMemberId(), eventId);
	}
}
