package com.ssafy.stargate.model.service;


import com.ssafy.stargate.model.dto.JwtResponseDto;
import com.ssafy.stargate.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * JWT 토큰 재생성 구현체
 * @author 김도현
 */
@Service
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenService{

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * refreshToken 을 바탕으로 accessToken 새로 생성
     * @param refreshToken String refreshToken
     * @return
     */
    @Override
    public JwtResponseDto create(String refreshToken) throws Exception {

        if(jwtTokenUtil.validateToken(refreshToken)){
            return JwtResponseDto.builder()
                    .refreshToken(refreshToken)
                    .accessToken(jwtTokenUtil.createAccessToken(jwtTokenUtil.getEmailFromToken(refreshToken), jwtTokenUtil.getAuthorityFromToken(refreshToken)))
                    .build();
        }else{
            throw new Exception();
        }


    }
}
