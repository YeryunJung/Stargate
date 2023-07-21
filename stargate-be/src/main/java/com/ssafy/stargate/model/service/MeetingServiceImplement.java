package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.common.*;
import com.ssafy.stargate.model.entity.*;
import com.ssafy.stargate.model.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImplement implements MeetingService {
    @Autowired
    private final MeetingRepository meetingRepository;

    @Autowired
    private final MeetingFUserRepository meetingFUserRepository;

    @Autowired
    private final MeetingMemberRepository meetingMemberRepository;

    @Autowired
    private final PMemberRepository pMemberRepository;

    @Autowired
    private final FUserRepository fUserRepository;

    /*
     TODO:
       - Exceptin 처리
       - 주석
     */

    @Override
    public MeetingDto create(MeetingDto dto, Principal principal) {
        String email = principal.getName();
        log.info("data : {}", dto);

        Meeting meeting = Meeting.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .waitingTime(dto.getWaitingTime())
                .meetingTime(dto.getMeetingTime())
                .notice(dto.getNotice())
                .image(dto.getImage())
                .build();

        meetingRepository.save(meeting);

        List<MeetingMemberBridge> meetingMembers = meetingMemberRepository.saveAll(dto.getMeetingMembers().stream().map(meetingMemberBridgeDto -> MeetingMemberBridge.builder()
                .meeting(meeting)
                .pMember(pMemberRepository.findById(meetingMemberBridgeDto.getMemberNo()).get()) // TODO[?] 이게 맞나..?
                .orderNum(meetingMemberBridgeDto.getOrderNum())
                .build()).toList());

        List<MeetingFUserBridge> meetingFUsers = meetingFUserRepository.saveAll(dto.getMeetingFUsers().stream().map(meetingFUserBridgeDto -> MeetingFUserBridge.builder()
                .meeting(meeting)
                .fUser(fUserRepository.findById(meetingFUserBridgeDto.getEmail()).get()) // TODO[?] 이게 맞나..?
                .orderNum(meetingFUserBridgeDto.getOrderNum())
                .build()).toList());

        return MeetingDto.builder()
                .uuid(meeting.getUuid())
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .waitingTime(meeting.getWaitingTime())
                .meetingTime(meeting.getMeetingTime())
                .notice(meeting.getNotice())
                .image(meeting.getImage())
                .meetingMembers(meetingMembers.stream()
                        .map(meetingMember -> MeetingMemberBridgeDto.builder()
                                .no(meetingMember.getNo())
                                .memberNo(meetingMember.getPMember().getMemberNo())
                                .orderNum(meetingMember.getOrderNum())
                                .build()).toList())
                .meetingFUsers(meetingFUsers.stream()
                        .map(meetingFUser -> MeetingFUserBridgeDto.builder()
                                .no(meetingFUser.getNo())
                                .email(meetingFUser.getFUser().getEmail())
                                .orderNum(meetingFUser.getOrderNum())
                                .build()).toList())
                .build();
    }

    /*
    @Override
    public MeetingMemberBridgeDto createMeetingMember(MeetingMemberBridgeDto dto, Principal principal) {
        return null;
    }

    @Override
    public MeetingFUserBridgeDto createMeetingFUser(MeetingFUserBridgeDto dto, Principal principal) {
        return null;
    }
     */
}
