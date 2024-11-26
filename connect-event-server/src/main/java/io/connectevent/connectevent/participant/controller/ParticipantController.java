package io.connectevent.connectevent.participant.controller;

import io.connectevent.connectevent.auth.dto.MemberSessionDto;
import io.connectevent.connectevent.auth.jwt.LoginMemberInfo;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.participant.dto.ParticipantListResponseDto;
import io.connectevent.connectevent.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/participants")
@Tag(name = "참가자 (Participant)", description = "참가자 관련 API")
@Logging
public class ParticipantController {

	private final ParticipantService participantService;

	@Operation(summary = "참가자 목록 조회", description = "참가자 목록을 조회합니다. 페이지네이션을 지원합니다.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/events/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public ParticipantListResponseDto getParticipants(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "eventId") Long eventId
	) {
			return participantService.getParticipants(eventId);
	}

	@Operation(summary = "이벤트 참여 요청", description = "이벤트 참여 요청을 합니다.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/events/{eventId}/request")
	@ResponseStatus(HttpStatus.CREATED)
	public void requestEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "eventId") Long eventId
	) {
			participantService.requestEvent(eventId, memberSessionDto.getMemberId());
	}

	@Operation(summary = "이벤트 참여 수락", description = "이벤트 참여 요청을 수락합니다. 이벤트 마스터만")
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/events/{eventId}/members/{participantId}/accept")
	@ResponseStatus(HttpStatus.CREATED)
	public void acceptEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "participantId") Long participantId
	) {
			participantService.acceptEvent(memberSessionDto.getMemberId(), eventId, participantId);
	}

	@Operation(summary = "이벤트 탈퇴", description = "이벤트 참여를 취소합니다. 방장이 아닌 경우만 가능")
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/events/{eventId}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelEvent(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "eventId") Long eventId
	) {
			participantService.cancelEvent(memberSessionDto.getMemberId(), eventId);
	}
}
