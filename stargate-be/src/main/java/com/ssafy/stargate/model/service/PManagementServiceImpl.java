package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.repository.PGroupRepository;
import com.ssafy.stargate.model.repository.PMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PManagementServiceImpl implements PManagmentService {
    @Autowired
    PMemberRepository memberRepository;
    @Autowired
    PGroupRepository groupRepository;



}
