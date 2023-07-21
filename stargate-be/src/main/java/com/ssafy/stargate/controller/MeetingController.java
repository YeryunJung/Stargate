package com.ssafy.stargate.controller;

import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.dto.common.MeetingFUserBridgeDto;
import com.ssafy.stargate.model.dto.common.MeetingMemberBridgeDto;
import com.ssafy.stargate.model.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/meetings")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MeetingController {
    @Autowired
    private final MeetingService meetingService;

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@ModelAttribute MeetingDto dto, Principal principal) {
        System.out.println(dto);
        MeetingDto meeting = meetingService.create(dto, principal);
        return ResponseEntity.status(201).body(meeting);
    }

    /*
    // TODO: API 작성하기
    // TODO: Json vs Form?
    @PostMapping("/member/create") // TODO: vs members?
    public ResponseEntity<?> createMeetingMember(@ModelAttribute MeetingMemberBridgeDto dto, Principal principal){
        MeetingMemberBridgeDto meetingMember = meetingService.createMeetingMember(dto, principal);
        return ResponseEntity.ok(meetingMember);
    }

    // TODO: Json vs Form?
    @PostMapping("/fuser/create") // TODO: fUsers?
    public ResponseEntity<?> createMeetingFUser(@ModelAttribute MeetingFUserBridgeDto dto, Principal principal){
        MeetingFUserBridgeDto meetingFUser = meetingService.createMeetingFUser(dto, principal);
        return ResponseEntity.ok(meetingFUser);
    }
*/
}
