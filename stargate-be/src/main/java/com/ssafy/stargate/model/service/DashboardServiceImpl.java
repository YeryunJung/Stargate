package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.dto.common.SavedFileDto;
import com.ssafy.stargate.model.dto.response.DashboardMeetingResponseDto;
import com.ssafy.stargate.model.dto.response.DashboardResponseDto;
import com.ssafy.stargate.model.entity.Meeting;
import com.ssafy.stargate.model.entity.MeetingFUserBridge;
import com.ssafy.stargate.model.repository.MeetingFUserRepository;
import com.ssafy.stargate.model.repository.MeetingRepository;
import com.ssafy.stargate.util.FileUtil;
import com.ssafy.stargate.util.TimeUtil;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 대시보드 미팅 정보 서비스 구현체
 */
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingFUserRepository meetingFUserRepository;

    @Autowired
    TimeUtil timeUtil;

    @Autowired
    private FileUtil fileUtil;

    @Value("${s3.filepath.meeting}")
    private String filePath;

    List<DashboardMeetingResponseDto> ongoingMeetings;

    List<DashboardMeetingResponseDto> expectedMeetings;

    List<DashboardMeetingResponseDto> finishedMeetings;

    LocalDateTime today;



    /**
     * 회원 (email) 와 관련된 meeting 정보 조회 
     * USER 일 경우 MeetingFUserBridge 를 통해서 Meeting 조회, PRODUCER 는 바로 Meeting 조회
     * @return DashboardResponseDto 과거, 현재, 미래 meeting 정보 저장하고 있는 dto
     * @throws NotFoundException 해당하는 AUTH 가 USER, PRODUCER 가 아닐 경우
     */
    @Override
    @Transactional
    public DashboardResponseDto getDashBoard() throws NotFoundException{

        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = securityContext.getAuthentication();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        ongoingMeetings = new ArrayList<>();
        expectedMeetings = new ArrayList<>();
        finishedMeetings = new ArrayList<>();
        today = LocalDateTime.now();

        String auth = usernamePasswordAuthenticationToken.getAuthorities().stream().toList().get(0).getAuthority().toString();
        String email = usernamePasswordAuthenticationToken.getName().toString();

        log.info("auth {} ", auth);
        log.info("email {} ", email);

        if(auth.equals("USER")){

            List<MeetingFUserBridge> meetingFUserBridgeList = meetingFUserRepository.findByEmail(email).orElse(null);

            if(meetingFUserBridgeList != null){
                for(MeetingFUserBridge meetingFUserBridge : meetingFUserBridgeList){

                    log.info("meetingFUserBridge {} ", meetingFUserBridge.getEmail());

                    Meeting meeting = meetingRepository.findById(meetingFUserBridge.getMeeting().getUuid()).orElse(null);
                    classifyMeetings(meeting);
                }
            }
        }else if(auth.equals("PRODUCER")){

            List<Meeting> meetingList = meetingRepository.findAllByEmail(email).orElse(null);

            if(meetingList != null){
                for(Meeting meeting : meetingList){
                    classifyMeetings(meeting);
                }
            }
        }else{
            throw new NotFoundException("해당하는 AUTH 는 유효하지 않습니다.");
        }
        return DashboardResponseDto.builder()
                .ongoing(ongoingMeetings)
                .expected(expectedMeetings)
                .finished(finishedMeetings)
                .build();
    }

    /**
     * meeting의 날짜와 오늘 날짜를 비교해 오늘 미팅, 과거 미팅, 미래 미팅으로 분류
     * @param meeting Meeting 미팅 정보
     */
    private void classifyMeetings(Meeting meeting){

        //          미팅 시작 - 현재 > 0 : 예정
        //          미팅 시작 - 현재 < 0:
        //              1) 진행 중 -> (미팅 시작 - 현재) + 총 미팅 시간 > 0 : 진행 중
        //              2) (미팅 시작 - 현재) + 총 미팅 시간 <= 0 : 완료
        //         총 미팅 시간 : ( 미팅 시간 + 대기시간 ) * 미팅 팬 유저(가입한 사람)
        
        if(meeting != null){

            long remainingSecond = timeUtil.getRemaingSeconds(meeting.getStartDate());
            long totalMeetingTime = (meeting.getMeetingTime() + meeting.getWaitingTime()) * meetingFUserRepository.countRegisteredFUsers(meeting.getUuid());

            if(remainingSecond> 0){
                expectedMeetings.add(setMeetingInfo(meeting , remainingSecond));
            }else{
                log.info("totalMeetingTime : {}", totalMeetingTime);
                log.info("remainingSecond + totalMeetingTime : {}", remainingSecond + totalMeetingTime);
                if(remainingSecond + totalMeetingTime > 0){
                    ongoingMeetings.add(setMeetingInfo(meeting, remainingSecond));
                }else{
                    finishedMeetings.add(setMeetingInfo(meeting, remainingSecond));
                }
            }
        }
    }

    /**
     * 미팅 정보 전달하기 위해 dto 에 정보 저장
     * @param meeting Meeting 미팅 정보
     * @return DashboardMeetingResponseDto 대시보드에 있는 미팅 정보 담는 dto
     */
    private DashboardMeetingResponseDto setMeetingInfo(Meeting meeting, long remainingSecond){

        SavedFileDto savedFileDto = fileUtil.getFileInfo(filePath, meeting.getImage());

        log.info("초 : {}",remainingSecond);

            return DashboardMeetingResponseDto.builder()
                    .uuid(meeting.getUuid())
                    .name(meeting.getName())
                    .startDate(meeting.getStartDate())
                    .remainingTime(remainingSecond)
                    .imageFileInfo(savedFileDto)
                    .build();
    }
}