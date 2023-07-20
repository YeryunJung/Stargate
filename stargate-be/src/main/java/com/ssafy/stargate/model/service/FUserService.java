package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.EmailDuplicationException;
import com.ssafy.stargate.exception.LoginException;
import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.common.FUserDto;
import com.ssafy.stargate.model.dto.common.FUserFindIdDto;
import com.ssafy.stargate.model.dto.request.FUserLoginRequestDto;
import com.ssafy.stargate.model.dto.response.JwtResponseDto;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

/**
 * 팬 유저 서비스 인터페이스
 * @author 남현실
 */
public interface FUserService {
    public void create(@Validated FUserDto dto) throws EmailDuplicationException, RegisterException;
    public JwtResponseDto login(FUserLoginRequestDto dto) throws LoginException;
    public FUserDto getFUser(Principal principal) throws Exception;
    public void updateFUser(FUserDto fUserDto);
    public void deleteFUser(FUserDto fUserDto);

    public FUserFindIdDto getFUserId(FUserFindIdDto dto);
}
