package io.connectevent.connectevent.mapper;

import io.connectevent.connectevent.event.domain.Event;
import io.connectevent.connectevent.event.domain.EventTag;
import io.connectevent.connectevent.event.dto.EventDetailDto;
import io.connectevent.connectevent.event.dto.EventListResponseDto;
import io.connectevent.connectevent.event.dto.EventPreviewDto;
import io.connectevent.connectevent.event.dto.EventTagDto;
import io.connectevent.connectevent.event.dto.TagDto;
import io.connectevent.connectevent.location.domain.Location;
import io.connectevent.connectevent.location.dto.LocationDto;
import io.connectevent.connectevent.tag.domain.Tag;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class EventMapper {

	@Mapping(source = "event.id", target = "eventId")
	@Mapping(source = "event.description", target = "descriptionPreview", qualifiedByName = "truncate")
	public abstract EventPreviewDto toEventPreviewDto(Event event);


	@Mapping(source = "eventPreviewDtos", target = "results")
	public abstract EventListResponseDto toEventListResponseDto(List<EventPreviewDto> eventPreviewDtos, int pageNumber, int totalElements, int totalPages, boolean b, boolean b1);

	@Mapping(source = "eventTag.id", target = "eventTagId")
	@Mapping(source = "eventTag.tag.id", target = "tagId")
	@Mapping(source = "eventTag.tag.name", target = "tagName")
	public abstract EventTagDto toEventTagDto(EventTag eventTag);

	@Mapping(source = "event.id", target = "eventId")
	@Mapping(source = "locationDto", target = "location")
	@Mapping(source = "eventTagDtos", target = "eventTags")
	public abstract EventDetailDto toEventDetailDto(Event event, LocationDto locationDto, List<EventTagDto> eventTagDtos);

	@Named("truncate")
	String truncateContent(String content) {
		return content != null && content.length() > 16 ? content.substring(0, 16).concat("...")
				: content;
	}

	@Mapping(source = "tag.id", target = "tagId")
	@Mapping(source = "tag.name", target = "tagName")
	public abstract TagDto toTagDto(Tag tag);
}
