package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.PUserRegisterRequestDto;
import com.ssafy.stargate.model.entity.PUser;
import com.ssafy.stargate.model.repository.PUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 소속사 유저완 관련된 서비스의 구현체
 *
 * @author 백승윤
 */
@Service
@Slf4j
public class PUserServiceImpl implements PUserService {

    @Autowired
    PUserRepository pUserRepository;

    @Override
    public void register(PUserRegisterRequestDto dto) throws RegisterException {
        try{

            PUser pUser = PUser.builder()
                    .email(dto.getEmail())
                    .code(dto.getCode())
                    .password(dto.getPassword()) // TODO : 비밀번호 암호화하기
                    .joinDate(LocalDateTime.now())
                    .build();
            pUserRepository.save(pUser);
        }catch (Exception e){
            log.error("소속사 회원가입 실패. 가입 데이터 : {}",dto);
            e.printStackTrace();
            throw new RegisterException();
        }
    }
}
