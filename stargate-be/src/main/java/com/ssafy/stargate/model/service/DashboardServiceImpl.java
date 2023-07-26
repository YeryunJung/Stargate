package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.common.MeetingDto;
import com.ssafy.stargate.model.dto.response.MeetingListResponseDto;
import com.ssafy.stargate.model.entity.Meeting;
import com.ssafy.stargate.model.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    MeetingRepository meetingRepository;
}
