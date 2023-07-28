package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.response.DashboardResponseDto;
import com.ssafy.stargate.model.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    MeetingRepository meetingRepository;

    @Override
    public DashboardResponseDto getDashBoard(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws NotFoundException{

        String auth = usernamePasswordAuthenticationToken.getAuthorities().stream().toList().get(0).getAuthority().toString();

        String email = usernamePasswordAuthenticationToken.getName().toString();

        log.info("auth {} ", auth);
        log.info("email {} ", email);

        if(auth.equals("USER")){




        }else if(auth.equals("PRODUCER")){


        }else{
            throw new NotFoundException("auth 가 유효하지 않습니다.");
        }

        return DashboardResponseDto.builder().build();
    }
}
