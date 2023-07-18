package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.FUserRegisterDTO;

public interface FUserService {
    public FUserRegisterDTO.Response create(FUserRegisterDTO.Request dto);

}
