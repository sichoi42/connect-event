package io.connectevent.connectevent.feedback.controller;

import io.connectevent.connectevent.auth.dto.MemberSessionDto;
import io.connectevent.connectevent.auth.jwt.LoginMemberInfo;
import io.connectevent.connectevent.feedback.dto.FeedbackCreateRequestDto;
import io.connectevent.connectevent.feedback.dto.FeedbackListResponseDto;
import io.connectevent.connectevent.feedback.dto.FeedbackUpdateRequestDto;
import io.connectevent.connectevent.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/v1/feedbacks")
@Tag(name = "피드백 (Feedback)", description = "피드백 관련 API")
public class FeedbackController {

	private final FeedbackService feedbackService;

	@Operation(summary = "피드백 목록 조회", description = "피드백 목록을 조회합니다. 페이지네이션을 지원합니다.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/events/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public FeedbackListResponseDto getFeedbacks(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable(value = "eventId") Long eventId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
			return feedbackService.getFeedbacks(eventId, PageRequest.of(page, size));
	}

	@Operation(summary = "피드백 등록", description = "피드백을 등록합니다.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/events/{eventId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createFeedback(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable(value = "eventId") Long eventId,
			@RequestBody FeedbackCreateRequestDto requestDto
	) {
		feedbackService.createFeedback(eventId, memberSessionDto.getMemberId(), requestDto);
	}

	@Operation(summary = "피드백 수정", description = "피드백을 수정합니다.")
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/events/{eventId}/feedbacks/{feedbackId}")
	@ResponseStatus(HttpStatus.OK)
	public void updateFeedback(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable(value = "eventId") Long eventId,
			@PathVariable(value = "feedbackId") Long feedbackId,
			@RequestBody FeedbackUpdateRequestDto requestDto
	) {
		feedbackService.updateFeedback(eventId, memberSessionDto.getMemberId(), feedbackId, requestDto);
	}

	@Operation(summary = "피드백 삭제", description = "피드백을 삭제합니다.")
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/events/{eventId}/feedbacks/{feedbackId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFeedback(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable(value = "eventId") Long eventId,
			@PathVariable(value = "feedbackId") Long feedbackId
	) {
		feedbackService.deleteFeedback(eventId, memberSessionDto.getMemberId(), feedbackId);
	}
}
