package io.connectevent.connectevent.location.service;

import io.connectevent.connectevent.location.domain.Location;
import io.connectevent.connectevent.location.dto.LocationCreateRequestDto;
import io.connectevent.connectevent.location.dto.LocationDto;
import io.connectevent.connectevent.location.dto.LocationListResponseDto;
import io.connectevent.connectevent.location.repository.LocationRepository;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.mapper.LocationMapper;
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
public class LocationService {

	private final LocationRepository locationRepository;
	private final LocationMapper locationMapper;

	public LocationListResponseDto getLocations(Pageable pageable) {
		Page<Location> locations = locationRepository.findAll(pageable);
		List<LocationDto> locationDtos = locations.stream()
				.map(locationMapper::toLocationDto)
				.toList();

		return locationMapper.toLocationListResponseDto(
				locationDtos,
				pageable.getPageNumber(),
				(int) locations.getTotalElements(),
				locations.getTotalPages(),
				locations.hasNext(),
				locations.hasPrevious()
		);
	}

	public LocationDto createLocation(LocationCreateRequestDto requestDto) {
		Location newLocation = Location.of(requestDto.getName(), requestDto.getCapacity(), requestDto.getAddress());
		Location savedLocation = locationRepository.save(newLocation);
		return locationMapper.toLocationDto(savedLocation);
	}
}
