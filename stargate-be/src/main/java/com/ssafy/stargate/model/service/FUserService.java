package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.FUserRegisterDto;

public interface FUserService {
    public FUserRegisterDto.Response create(FUserRegisterDto.Request dto) throws RegisterException;

}
