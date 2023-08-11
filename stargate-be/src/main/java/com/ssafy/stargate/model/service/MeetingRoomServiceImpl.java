package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.response.meetingroom.MeetingRoomFUserResponseDto;
import com.ssafy.stargate.model.dto.response.meetingroom.MeetingRoomMemberResponseDto;
import com.ssafy.stargate.model.entity.*;
import com.ssafy.stargate.model.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * 미팅룸 서비스 구현체
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingRoomServiceImpl implements MeetingRoomService {

    private final MeetingRepository meetingRepository;
    private final FUserRepository fUserRepository;
    private final PolaroidEnableRepository polaroidEnableRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final PostitRepository postitRepository;

    @Override
    public MeetingRoomFUserResponseDto getFUserMeetingRoomInfo(UUID uuid, Principal principal) {
        return null;
    }

    /**
     * 연예인이 방 입장시 필요한 모든 정보를 일괄 로딩하여 배치한다.
     *
     * @param roomId 연예인 대기방 고유번호
     * @return 내가 봐도 코드가 넘 더럽당.
     */
    @Override
    @Transactional(readOnly = true)
    public MeetingRoomMemberResponseDto getMemberMeetingRoomInfo(String roomId) {
        StringTokenizer stk = new StringTokenizer(roomId, ".");
        UUID meetingUuid = UUID.fromString(stk.nextToken());
        UUID meetingMemberBridgeUuid = UUID.fromString(stk.nextToken());
        long memberNo = meetingMemberRepository.getMemberNoById(meetingMemberBridgeUuid);
        log.info("MEETING UUID = {}, meetingMemberBridgeUuid = {}, memberNo = {}", meetingUuid, meetingMemberBridgeUuid,memberNo);
        Meeting meeting = meetingRepository.findById(meetingUuid).orElseThrow();
        List<String> fUserList = meetingRepository.getFUserListByMeetingId(meetingUuid);
        log.info("FUSER LIST = {}", fUserList);
        List<FUser> fUserEntityList = fUserRepository.findAllById(fUserList);
        List<MeetingRoomMemberResponseDto.InnerMeetingFUser> innerMeetingFUserList =
                fUserEntityList.stream().map(fUser -> MeetingRoomMemberResponseDto.InnerMeetingFUser
                        .builder()
                        .email(fUser.getEmail())
                        .name(fUser.getName())
                        .nickname(fUser.getNickname())
                        .birthday(fUser.getBirthday())
                        .polaroidEnable(Boolean.parseBoolean(
                                        polaroidEnableRepository.findById(
                                                PolaroidEnable.createId(meetingUuid, fUser.getEmail(), memberNo)).orElse(PolaroidEnable.builder()
                                                .isPolaroidEnable("false").build()
                                        ).getIsPolaroidEnable()
                                )
                        )
                        .postitContents(postitRepository.findPostit(fUser.getEmail(),memberNo,meetingUuid).orElse(Postit.builder().contents("등록된 포스트잇이 없습니다.").build()).getContents())
                        .totalMeetingCnt(meetingRepository.countByEmailAndMemberNo(fUser.getEmail(),memberNo))
                        .build()).toList();
        MeetingRoomMemberResponseDto result = MeetingRoomMemberResponseDto.builder()
                .waitingTime(meeting.getWaitingTime())
                .meetingTime(meeting.getMeetingTime())
                .photoNum(meeting.getPhotoNum())
                .memberNo(memberNo)
                .meetingFUsers(innerMeetingFUserList)
                .build();
        log.info("meeting member response dto = {}", result);
        return result;

        // 팬유저정보
        // 폴라로이드 가용 여부 -> redis
        // 포스트잇 내용
        // 총 팬미팅 횟수
    }
}
