package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.dto.response.DashboardResponseDto;
import com.ssafy.stargate.model.entity.Meeting;
import com.ssafy.stargate.model.entity.MeetingFUserBridge;
import com.ssafy.stargate.model.repository.MeetingFUserRepository;
import com.ssafy.stargate.model.repository.MeetingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingFUserRepository meetingFUserRepository;

    List<MeetingDto> todayMeetings;

    List<MeetingDto> futureMeetings;

    List<MeetingDto> pastMeetings;

    LocalDateTime today;

    /**
     * 회원 (email) 와 관련된 meeting 정보 조회 
     * USER 일 경우 MeetingFUserBridge 를 통해서 Meeting 조회, PRODUCER 는 바로 Meeting 조회
     * @param usernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken 인증 정보 담는 dto
     * @return DashboardResponseDto 과거, 현재, 미래 meeting 정보 저장하고 있는 dto
     * @throws NotFoundException 해당하는 AUTH 가 USER, PRODUCER 가 아닐 경우
     */
    @Override
    public DashboardResponseDto getDashBoard(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws NotFoundException{

        todayMeetings = new ArrayList<>();

        futureMeetings = new ArrayList<>();

        pastMeetings = new ArrayList<>();

        today = LocalDateTime.now();

        String auth = usernamePasswordAuthenticationToken.getAuthorities().stream().toList().get(0).getAuthority().toString();

        String email = usernamePasswordAuthenticationToken.getName().toString();

        log.info("auth {} ", auth);

        log.info("email {} ", email);

        if(auth.equals("USER")){

            List<MeetingFUserBridge> meetingFUserBridgeList = meetingFUserRepository.findByfUserEmail(email).orElseThrow();

            if(meetingFUserBridgeList != null){

                for(MeetingFUserBridge meetingFUserBridge : meetingFUserBridgeList){

                    Meeting meeting = meetingFUserRepository.findByMeeting_Uuid(meetingFUserBridge.getMeeting().getUuid()).orElse(null);

                    classifyMeetings(meeting);
                }
            }
        }else if(auth.equals("PRODUCER")){

            List<Meeting> meetingList = meetingRepository.findAllByEmail(email).orElse(null);

            for(Meeting meeting : meetingList){
                classifyMeetings(meeting);
            }
        }else{

            throw new NotFoundException("해당하는 AUTH 는 유효하지 않습니다.");
        }

        return DashboardResponseDto.builder()
            .today(todayMeetings)
            .past(pastMeetings)
            .future(futureMeetings)
            .build();
    }

    /**
     * meeting의 날짜와 오늘 날짜를 비교해 오늘 미팅, 과거 미팅, 미래 미팅으로 분류
     * @param meeting Meeting 미팅 정보
     */
    private void classifyMeetings(Meeting meeting){

        if(meeting != null ){

            if(meeting.getStartDate().getDayOfYear() == today.getDayOfYear()){
                todayMeetings.add(MeetingDto.entityToDto(meeting));

            }else if(meeting.getStartDate().isBefore(today)){
                pastMeetings.add(MeetingDto.entityToDto(meeting));

            }else if(meeting.getStartDate().isAfter(today)){
                futureMeetings.add(MeetingDto.entityToDto(meeting));
            }
        }
    }
}
