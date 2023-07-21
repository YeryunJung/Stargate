package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.SaveException;
import com.ssafy.stargate.model.dto.common.MeetingDto;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * 미팅 관련 서비스 인터페이스
 */
public interface MeetingService {

    public List<MeetingDto> getMeetingList(MeetingDto dto, Principal principal);

    public MeetingDto getMeeting(UUID uuid, Principal principal);

    public MeetingDto createMeeting(MeetingDto dto, Principal principal) throws SaveException;

    public void updateMeeting(MeetingDto dto, Principal principal);

    public void deleteMeeting(MeetingDto dto, Principal principal);
}
