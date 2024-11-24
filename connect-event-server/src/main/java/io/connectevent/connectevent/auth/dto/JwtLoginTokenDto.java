package io.connectevent.connectevent.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Getter
@Builder
@FieldNameConstants
public class JwtLoginTokenDto {

	private String accessToken;
}
