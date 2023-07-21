package com.ssafy.stargate.controller;

import com.ssafy.stargate.exception.SaveException;
import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * 미팅 관리를 수행하는 컨트롤러
 */
@RequestMapping("/meetings")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MeetingController {
    @Autowired
    private final MeetingService meetingService;

    @GetMapping("/get")
    public ResponseEntity<List<MeetingDto>> getMeetings(@ModelAttribute MeetingDto dto, Principal principal) throws SaveException {
        List<MeetingDto> meeting = meetingService.getMeetingList(dto, principal);
        return ResponseEntity.status(200).body(meeting);
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<?> getMeeting(@PathVariable UUID uuid, Principal principal) throws SaveException {
        MeetingDto meeting = meetingService.getMeeting(uuid, principal);
        return ResponseEntity.status(200).body(meeting);
    }

    /**
     * 신규 미팅을 생성한다.
     * @param dto [MeetingDto] 생성할 신규 미팅 정보
     * @param principal [Principal] 소속사 이메일이 포함된 객체
     * @return 저장된 미팅 결과 dto (성공: 201)
     * @throws SaveException
     */
    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@ModelAttribute MeetingDto dto, Principal principal) throws SaveException {
        MeetingDto meeting = meetingService.createMeeting(dto, principal);
        return ResponseEntity.status(201).body(meeting);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMeeting(@ModelAttribute MeetingDto dto, Principal principal) throws SaveException {
        meetingService.updateMeeting(dto, principal);
        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMeeting(@ModelAttribute MeetingDto dto, Principal principal) throws SaveException {
        meetingService.deleteMeeting(dto, principal);
        return ResponseEntity.status(200).body(null);
    }
}