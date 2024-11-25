package io.connectevent.connectevent.auth.service;

import io.connectevent.connectevent.auth.dto.EmailLoginRequestDto;
import io.connectevent.connectevent.auth.dto.EmailRegisterRequestDto;
import io.connectevent.connectevent.auth.dto.JwtLoginTokenDto;
import io.connectevent.connectevent.auth.jwt.JwtTokenProvider;
import io.connectevent.connectevent.exception.status.AuthExceptionStatus;
import io.connectevent.connectevent.log.Logging;
import io.connectevent.connectevent.member.domain.Member;
import io.connectevent.connectevent.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Logging
@Transactional
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	public void registerEmail(EmailRegisterRequestDto requestDto) {
//		1. 이메일 중복 확인
		if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
			throw AuthExceptionStatus.MEMBER_ALREADY_EXISTS.toServiceException();
		}

//		2. 회원가입
		LocalDateTime now = LocalDateTime.now();
		Member member = Member.of(
				requestDto.getName(),
				requestDto.getEmail(),
				requestDto.getPassword(),
				now
		);
		memberRepository.save(member);
	}

	public JwtLoginTokenDto loginEmail(EmailLoginRequestDto requestDto) {
//		1. 이메일로 회원 조회
		Optional<Member> member = memberRepository.findByEmail(requestDto.getEmail());

//		2. 존재하지 않는 회원인 경우
		if (member.isEmpty()) {
			throw AuthExceptionStatus.MEMBER_NOT_FOUND.toServiceException();
		}

//		3. 이메일 혹은 비밀번호가 틀린 경우
		if (!member.get().getEmail().equals(requestDto.getEmail())
				|| !member.get().getPassword().equals(requestDto.getPassword())) {
			throw AuthExceptionStatus.EMAIL_OR_PASSWORD_INCORRECT.toServiceException();
		}

//		4. JWT 토큰 생성
		Jwt jwt = jwtTokenProvider.createMemberAccessToken(member.get().getId());
		return JwtLoginTokenDto.builder()
				.accessToken(jwt.getTokenValue())
				.build();
	}
}
