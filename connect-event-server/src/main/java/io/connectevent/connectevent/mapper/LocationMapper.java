package io.connectevent.connectevent.mapper;

import io.connectevent.connectevent.location.domain.Location;
import io.connectevent.connectevent.location.dto.LocationDto;
import io.connectevent.connectevent.location.dto.LocationListResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

	@Mapping(source="location.id", target="locationId")
	LocationDto toLocationDto(Location location);

	@Mapping(source = "locationDtos", target = "results")
	LocationListResponseDto toLocationListResponseDto(List<LocationDto> locationDtos, int pageNumber, int totalElements, int totalPages, boolean b, boolean b1);
}
