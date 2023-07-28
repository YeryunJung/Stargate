package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.response.DashboardResponseDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface DashboardService {

    public DashboardResponseDto getDashBoard(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws NotFoundException;
}
