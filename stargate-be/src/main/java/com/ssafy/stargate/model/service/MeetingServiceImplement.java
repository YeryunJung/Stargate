package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.common.*;
import com.ssafy.stargate.model.entity.Meeting;
import com.ssafy.stargate.model.entity.PGroup;
import com.ssafy.stargate.model.entity.PMember;
import com.ssafy.stargate.model.entity.PUser;
import com.ssafy.stargate.model.repository.FUserRepository;
import com.ssafy.stargate.model.repository.MeetingRepository;
import com.ssafy.stargate.model.repository.PMemberRepository;
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
    private final FUserRepository fUserRepository;

    @Autowired
    private final PMemberRepository pMemberRepository;

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

//        String email = principal.getName();
//        log.info("data : {}", dto);
//        PGroup pGroup = PGroup.builder()
//                .name(dto.getName())
//                .pUser(PUser.builder().email(email).build())
//                .build();
//        groupRepository.save(pGroup);
//        List<PMember> newMembers = memberRepository.saveAll(dto.getMembers().stream().map(pMemberDto -> PMember.builder()
//                .pGroup(pGroup)
//                .name(pMemberDto.getName())
//                .build()
//        ).toList());
//        return PGroupDto.builder()
//                .groupNo(pGroup.getGroupNo())
//                .name(pGroup.getName())
//                .members(newMembers.stream()
//                        .map(pMember -> PMemberDto.builder()
//                                .name(pMember.getName()).
//                                memberNo(pMember.getMemberNo())
//                                .build())
//                        .toList())
//                .build();
//        meetingRepository.save(meeting);
//        dto.setUuid(meeting.getUuid());
        return dto;
    }

    @Override
    public MeetingMemberBridgeDto createMeetingMember(MeetingMemberBridgeDto dto, Principal principal) {
        return null;
    }

    @Override
    public MeetingFUserBridgeDto createMeetingFUser(MeetingFUserBridgeDto dto, Principal principal) {
        return null;
    }
//
//    public Meeting createMeetingWithMembersAndFans(Meeting meeting, List<FUser> fans, List<PMember> members) {
//        Meeting savedMeeting = meetingRepository.save(meeting);
//
//        for (FUser fan : fans) {
//            MeetingFanBridge meetingFanBridge = MeetingFanBridge.builder()
//                    .fUser(fan)
//                    .meeting(savedMeeting)
//                    .build();
//            savedMeeting.getFUsers().add(meetingFanBridge);
//        }
//
//        for (PMember member : members) {
//            MeetingMemberBridge meetingMemberBridge = MeetingMemberBridge.builder()
//                    .pMember(member)
//                    .meeting(savedMeeting)
//                    .build();
//            savedMeeting.getMembers().add(meetingMemberBridge);
//        }
//
//        return savedMeeting;
//    }
}
