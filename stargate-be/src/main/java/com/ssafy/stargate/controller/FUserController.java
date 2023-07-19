package com.ssafy.stargate.controller;

import com.ssafy.stargate.exception.LoginException;
import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.request.FUserLoginRequestDto;
import com.ssafy.stargate.model.dto.request.FUserRegisterRequestDto;
import com.ssafy.stargate.model.dto.response.JwtResponseDto;
import com.ssafy.stargate.model.service.FUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
TODO
 - Exception 세분화
 - 주석 달기
 - 다른 Exception에 BaseException 적용
 - GlobalExceptionHandler로 모든 런타임 에러가 처리될 수 있도록 수정
 - 관련없는 패키지 삭제
 */

/**
 * 팬 유저에 관한 Controller이다.
 * 회원가입, 로그인을 지원한다.
 */
@RestController
@RequestMapping("/fusers")
@RequiredArgsConstructor
@Slf4j
public class FUserController {
    private final FUserService fUserService;

    /**
     * 팬 유저 회원가입
     *
     * @param dto [FUserRegisterRequestDto] 팬 유저 회원가입 request
     * @return [ResponseEntity<?>]성공: [200], 실패: [600]
     * @throws RegisterException 회원가입 등록 실패
     */
    @PostMapping("/register")
    public ResponseEntity<?> createFUsers(@ModelAttribute FUserRegisterRequestDto dto) throws RegisterException {
        fUserService.create(dto);
        return ResponseEntity.ok(null);
    }

    /**
     * 팬 로그인
     *
     * @param dto [FUserLoginRequestDto] 팬 로그인 request
     * @return [ResponseEntity<JwtResponseDto>] 성공: [200] JWT Response, 실패: [401]
     * @throws LoginException
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginFUsers(@ModelAttribute FUserLoginRequestDto dto) throws LoginException {
        try {
            return ResponseEntity.ok(fUserService.login(dto));
        } catch (LoginException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
