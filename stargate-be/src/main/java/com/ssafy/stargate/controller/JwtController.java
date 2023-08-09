package com.ssafy.stargate.controller;


import com.ssafy.stargate.exception.InvalidTokenException;
import com.ssafy.stargate.model.dto.response.JwtResponseDto;
import com.ssafy.stargate.model.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * JWT 토큰 다시 생성 Controller
 *
 * @author 김도현
 */
@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {
    private final JwtTokenService jwtTokenService;

    /**
     * refreshToken 을 받아서 만료된 accessToken 반환
     *
     * @param token HashMap<String, Object> refreshToken
     * @return [ResponseEntity<JwtResponseDto>] 기존의 refreshToken 과 새로운 accessToken 을 담는 dto (실패 : 600)
     * @throws InvalidTokenException 유효하지 않은 토큰 에러
     */
    @PostMapping("/new-access-token")
    public ResponseEntity<JwtResponseDto> createNewToken(@RequestBody HashMap<String, Object> token) throws InvalidTokenException {

        String refreshToken = (String) token.get("refreshToken");
        return ResponseEntity.ok(jwtTokenService.createAccessToken(refreshToken));
    }
}
