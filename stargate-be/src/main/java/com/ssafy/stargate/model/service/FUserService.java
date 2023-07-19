package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.FUserRegisterDto;

/**
 * 팬 유저 서비스 인터페이스
 */
public interface FUserService {
    public FUserRegisterDto.Response create(FUserRegisterDto.Request dto) throws RegisterException;

}
