package io.connectevent.connectevent.auth.controller;

import io.connectevent.connectevent.auth.dto.EmailLoginRequestDto;
import io.connectevent.connectevent.auth.dto.EmailRegisterRequestDto;
import io.connectevent.connectevent.auth.dto.JwtLoginTokenDto;
import io.connectevent.connectevent.auth.password.annotation.OneWayEncryption;
import io.connectevent.connectevent.auth.password.annotation.TargetMapping;
import io.connectevent.connectevent.auth.service.AuthService;
import io.connectevent.connectevent.log.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "인증 (Auth)", description = "인증 관련 API")
@Logging
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "이메일 회원가입", description = "이메일 회원가입을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "accepted",
                    content = @Content(schema = @Schema(implementation = JwtLoginTokenDto.class))
            ),
    })
    @PostMapping(value = "/email-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OneWayEncryption({
            @TargetMapping(clazz = EmailRegisterRequestDto.class, fields = {
                    EmailRegisterRequestDto.Fields.password})
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void registerEmail(
            @Valid @ModelAttribute EmailRegisterRequestDto requestDto
    ) {
        authService.registerEmail(requestDto);
    }

    @Operation(summary = "이메일 로그인", description = "이메일 로그인을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok")
    })
    @PostMapping(value = "/email-login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OneWayEncryption({
            @TargetMapping(clazz = EmailLoginRequestDto.class, fields = {
                    EmailLoginRequestDto.Fields.password})
    })
    public JwtLoginTokenDto loginEmail(
            @Valid @ModelAttribute EmailLoginRequestDto requestDto
    ) {
        return authService.loginEmail(requestDto);
    }
}
