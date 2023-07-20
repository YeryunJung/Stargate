package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.dto.common.MeetingFUserBridgeDto;
import com.ssafy.stargate.model.dto.common.MeetingMemberBridgeDto;

import java.security.Principal;

public interface MeetingService {
    public MeetingDto create(MeetingDto dto, Principal principal);

    MeetingMemberBridgeDto createMeetingMember(MeetingMemberBridgeDto dto, Principal principal);

    MeetingFUserBridgeDto createMeetingFUser(MeetingFUserBridgeDto dto, Principal principal);
}
