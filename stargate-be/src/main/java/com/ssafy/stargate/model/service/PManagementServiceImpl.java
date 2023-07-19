package com.ssafy.stargate.model.service;

import com.ssafy.stargate.model.dto.response.PGroupResponseDto;
import com.ssafy.stargate.model.entity.PGroup;
import com.ssafy.stargate.model.repository.PGroupRepository;
import com.ssafy.stargate.model.repository.PMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class PManagementServiceImpl implements PManagementService {
    @Autowired
    PMemberRepository memberRepository;
    @Autowired
    PGroupRepository groupRepository;


    @Override
    public List<PGroupResponseDto> getGroupList(Principal principal) {
        String email = principal.getName();
        log.info("PUSER EMAIL = {}",email);
        List<PGroup> groups = groupRepository.findAllByEmail(email);
        return groups.stream().map((pGroup -> PGroupResponseDto.builder()
                .groupNo(pGroup.getGroupNo())
                .name(pGroup.getName())
                .build()
        )).toList();
    }
}
