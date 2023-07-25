package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.SaveException;
import com.ssafy.stargate.model.dto.common.*;
import com.ssafy.stargate.model.entity.*;
import com.ssafy.stargate.model.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
TODO
 - [] Exception 추가하기
 - [] 테스트 해보기
 - [o] API 수정하기
 - [] API 수정한대로 수정
 - [] 방송 UID 만드는 부분 / 보내는 부분 추가
 - [] getMeeting에 유저상태 정보 추가
 - [o] user 관련 수정
 - [] Multipart로 이미지 받아서 저장해보기
 */

/**
 * 미팅 관련 서비스 구현체
 */
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
    private final PUserRepository pUserRepository;

    @Override
    public List<MeetingDto> getMeetingList(MeetingDto dto, Principal principal) {
        log.info("data : {}", dto);
        String email = principal.getName();

        List<Meeting> meetings = meetingRepository.findAllByEmail(email);
        return meetings.stream().map((meeting -> MeetingDto.builder()
                        .uuid(meeting.getUuid())
                        .name(meeting.getName())
                        .startDate(meeting.getStartDate())
                        .build()
                ))
                .toList();
    }

    @Override
    public MeetingDto getMeeting(UUID uuid, Principal principal) {
        log.info("data : {}", uuid);
        String email = principal.getName();

        Meeting meeting = meetingRepository.findByIdAndEmail(uuid, email);
        return entityToDtoMeeting(meeting);
    }

    /**
     * 신규 meeting을 생성한다.
     *
     * @param dto       [MeetingDto] 입력받은 meeting dto
     * @param principal [Principal] 소속사 이메일이 포함된 객체
     * @return [MeetingDto] 생성한 미팅 정보를 담은 dto
     */
    @Transactional
    @Override
    public MeetingDto createMeeting(MeetingDto dto, Principal principal) throws SaveException {
        log.info("data : {}", dto);
        String email = principal.getName();

        try {
            Meeting meeting = Meeting.builder()
                    .name(dto.getName())
                    .startDate(dto.getStartDate())
                    .waitingTime(dto.getWaitingTime())
                    .meetingTime(dto.getMeetingTime())
                    .notice(dto.getNotice())
                    .pUser(pUserRepository.findById(email).get())
                    .build();

            List<MeetingMemberBridge> meetingMembers = meetingMemberRepository.saveAll(dto.getMeetingMembers().stream().map(meetingMemberBridgeDto -> MeetingMemberBridge.builder()
                    .meeting(meeting)
                    .pMember(pMemberRepository.findById(meetingMemberBridgeDto.getMemberNo()).get())
                    .orderNum(meetingMemberBridgeDto.getOrderNum())
                    .build()).toList());

            List<MeetingFUserBridge> meetingFUsers = meetingFUserRepository.saveAll(dto.getMeetingFUsers().stream().map(meetingFUserBridgeDto -> MeetingFUserBridge.builder()
                    .meeting(meeting)
                    .email(meetingFUserBridgeDto.getEmail())
                    .orderNum(meetingFUserBridgeDto.getOrderNum())
                    .build()).toList());

            meeting.setMeetingMembers(meetingMembers);
            meeting.setMeetingFUsers(meetingFUsers);

            meetingRepository.save(meeting);

            return entityToDtoMeeting(meeting);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveException("미팅 생성 도중 오류가 발생했습니다.");
        }
    }

    @Transactional
    @Override
    public void updateMeeting(MeetingDto dto, Principal principal) {
        log.info("data : {}", dto);
        String email = principal.getName();
        Meeting meeting = meetingRepository.findByIdAndEmail(dto.getUuid(), email);
        meeting.setName(dto.getName());
        meeting.setStartDate(dto.getStartDate());
        meeting.setWaitingTime(dto.getWaitingTime());
        meeting.setMeetingTime(dto.getMeetingTime());
        meeting.setNotice(dto.getNotice());

        List<MeetingMemberBridge> meetingMembers = meeting.getMeetingMembers();
        List<MeetingMemberBridge> newMeetingMembers = dto.getMeetingMembers().stream().map(meetingMemberBridgeDto -> MeetingMemberBridge.builder()
                .meeting(meeting)
                .pMember(pMemberRepository.findById(meetingMemberBridgeDto.getMemberNo()).get())
                .orderNum(meetingMemberBridgeDto.getOrderNum())
                .build()).toList();
        updateMeetingMemberList(meetingMembers, newMeetingMembers);

        List<MeetingFUserBridge> meetingFUsers = meeting.getMeetingFUsers();
        List<MeetingFUserBridge> newMeetingFUsers = dto.getMeetingFUsers().stream().map(meetingFUserBridgeDto -> MeetingFUserBridge.builder()
                .meeting(meeting)
                .email(meetingFUserBridgeDto.getEmail())
                .orderNum(meetingFUserBridgeDto.getOrderNum())
                .build()).toList();
        updateMeetingFUserList(meetingFUsers, newMeetingFUsers);

    }

    @Transactional
    @Override
    public void deleteMeeting(MeetingDto dto, Principal principal) {
        log.info("data : {}", dto);
        String email = principal.getName();
        meetingRepository.deleteByIdAndEmail(dto.getUuid(), email);
    }


    private MeetingDto entityToDtoMeeting(Meeting meeting) {
        return MeetingDto.builder()
                .uuid(meeting.getUuid())
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .waitingTime(meeting.getWaitingTime())
                .meetingTime(meeting.getMeetingTime())
                .notice(meeting.getNotice())
                .meetingMembers(entityToDtoMeetingMemberList(meeting.getMeetingMembers()))
                .meetingFUsers(entityToDtoMeetingFUserList(meeting.getMeetingFUsers()))
                .build();
    }

    private List<MeetingMemberBridgeDto> entityToDtoMeetingMemberList(List<MeetingMemberBridge> meetingMembers) {
        return meetingMembers.stream()
                .map(meetingMember -> MeetingMemberBridgeDto.builder()
                        .no(meetingMember.getNo())
                        .memberNo(meetingMember.getPMember().getMemberNo())
                        .orderNum(meetingMember.getOrderNum())
                        .build()).toList();
    }

    private List<MeetingFUserBridgeDto> entityToDtoMeetingFUserList(List<MeetingFUserBridge> meetingFUsers) {
        return meetingFUsers.stream()
                .map(meetingFUser -> MeetingFUserBridgeDto.builder()
                        .no(meetingFUser.getNo())
                        .email(meetingFUser.getEmail())
                        .orderNum(meetingFUser.getOrderNum())
                        .build()).toList();
    }

    public void updateMeetingMemberList(List<MeetingMemberBridge> source, List<MeetingMemberBridge> target) {
        // create or update (target)
        for (MeetingMemberBridge tg : target) {
            Optional<MeetingMemberBridge> meetingMemberOptional = source.stream()
                    .filter(org -> org.getPMember().getMemberNo() == tg.getPMember().getMemberNo())
                    .findFirst();

            if (meetingMemberOptional.isPresent()) {
                MeetingMemberBridge meetingMember = meetingMemberOptional.get();
                // update source
                meetingMember.setOrderNum(tg.getOrderNum());
                meetingMemberRepository.save(meetingMember);
            } else {
                // create target
                meetingMemberRepository.save(tg);
            }
        }

        // delete (source)
        Iterator<MeetingMemberBridge> iterator = source.iterator();
        while (iterator.hasNext()) {
            MeetingMemberBridge src = iterator.next();
            boolean isExist = false;
            for (MeetingMemberBridge tg : target) {
                if (src.getPMember().getMemberNo() == tg.getPMember().getMemberNo()) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                meetingMemberRepository.deleteById(src.getNo());
                iterator.remove();
            }
        }
    }

    public void updateMeetingFUserList(List<MeetingFUserBridge> source, List<MeetingFUserBridge> target) {
        // create or update (target)
        for (MeetingFUserBridge tg : target) {
            Optional<MeetingFUserBridge> meetingFUserOptional = source.stream()
                    .filter(org -> org.getEmail().equals(tg.getEmail()))
                    .findFirst();

            if (meetingFUserOptional.isPresent()) {
                MeetingFUserBridge meetingFuser = meetingFUserOptional.get();
                // update source
                meetingFuser.setOrderNum(tg.getOrderNum());
                meetingFUserRepository.save(meetingFuser);
            } else {
                // create target
                meetingFUserRepository.save(tg);
            }
        }

        // delete (source)
        Iterator<MeetingFUserBridge> iterator = source.iterator();
        while (iterator.hasNext()) {
            MeetingFUserBridge src = iterator.next();
            boolean isExist = false;
            for (MeetingFUserBridge tg : target) {
                if (src.getEmail().equals(tg.getEmail())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                meetingFUserRepository.deleteById(src.getNo());
                iterator.remove();
            }
        }
    }
}
