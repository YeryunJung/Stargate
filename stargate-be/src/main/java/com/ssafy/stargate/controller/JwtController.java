package com.ssafy.stargate.controller;


import com.ssafy.stargate.exception.InvalidTokenException;
import com.ssafy.stargate.model.dto.response.JwtResponseDto;
import com.ssafy.stargate.model.service.JwtTokenService;
import com.ssafy.stargate.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * JWT 토큰 다시 생성 Controller
 * @author 김도현
 */
@RequestMapping("/jwt")
@RestController
@Slf4j
public class JwtController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtTokenService jwtTokenService;

    /**
     * refreshToken 을 받아서 만료된 accessToken 반환
     * @param token HashMap<String, Object> refreshToken
     * @return [ResponseEntity<JwtResponseDto>] 성공: [200] JWT Response, 실패: [600]
     */
    @PostMapping("/new-access-token")
    public ResponseEntity<JwtResponseDto> createNewToken(@RequestBody HashMap<String, Object> token){

        try {
            String refreshToken = (String) token.get("refreshToken");
            return ResponseEntity.ok(jwtTokenService.createAccessToken(refreshToken));
        }catch (InvalidTokenException e){
            return ResponseEntity.status(e.getStatus()).build();
        }
    }
}
