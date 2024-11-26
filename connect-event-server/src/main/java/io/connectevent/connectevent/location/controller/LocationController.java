package io.connectevent.connectevent.location.controller;

import io.connectevent.connectevent.auth.dto.MemberSessionDto;
import io.connectevent.connectevent.auth.jwt.LoginMemberInfo;
import io.connectevent.connectevent.exception.annotation.ApiErrorCodeExample;
import io.connectevent.connectevent.exception.status.LocationExceptionStatus;
import io.connectevent.connectevent.location.dto.LocationCreateRequestDto;
import io.connectevent.connectevent.location.dto.LocationDto;
import io.connectevent.connectevent.location.dto.LocationListResponseDto;
import io.connectevent.connectevent.location.service.LocationService;
import io.connectevent.connectevent.log.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/locations")
@Tag(name = "장소 (Location)", description = "장소 관련 API")
@Logging
public class LocationController {

	private final LocationService locationService;

	@Operation(summary = "장소 목록 조회", description = "장소 목록을 조회합니다. 페이지네이션을 지원합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public LocationListResponseDto getLocations(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
			return locationService.getLocations(PageRequest.of(page, size));
	}

	@Operation(summary = "신규 장소 등록", description = "신규 장소를 등록합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@ApiErrorCodeExample(
			locationExceptionStatuses = {
					LocationExceptionStatus.LOCATION_NAME_LENGTH_EXCEEDED,
					LocationExceptionStatus.LOCATION_ALREADY_EXISTS,
					LocationExceptionStatus.LOCATION_ADDRESS_LENGTH_EXCEEDED,
					LocationExceptionStatus.LOCATION_DESCRIPTION_LENGTH_EXCEEDED,
			}
	)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public LocationDto createLocation(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@Valid @RequestBody LocationCreateRequestDto requestDto
	) {
		return locationService.createLocation(requestDto);
	}
}
