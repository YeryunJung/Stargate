package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.response.PGroupResponseDto;

import java.security.Principal;
import java.util.List;

public interface PManagementService {
    List<PGroupResponseDto> getGroupList(Principal principal);
}
