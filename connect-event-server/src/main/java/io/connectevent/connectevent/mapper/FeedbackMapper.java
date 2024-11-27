package io.connectevent.connectevent.mapper;

import io.connectevent.connectevent.feedback.domain.Feedback;
import io.connectevent.connectevent.feedback.dto.FeedbackDto;
import io.connectevent.connectevent.feedback.dto.FeedbackListResponseDto;
import io.connectevent.connectevent.member.domain.Member;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

	@Mapping(source = "feedback.id", target = "feedbackId")
	@Mapping(source = "member.id", target = "memberId")
	@Mapping(source = "member.name", target = "memberName")
	@Mapping(source = "member.email", target = "memberEmail")
	FeedbackDto toFeedbackDto(Feedback feedback, Member member);

	@Mapping(source = "feedbackDtos", target = "results")
	FeedbackListResponseDto toFeedbackListResponseDto(List<FeedbackDto> feedbackDtos, int pageNumber, int totalElements, int totalPages, boolean b, boolean b1);
}
