package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.FUserRegisterDto;
import com.ssafy.stargate.model.entity.FUser;
import com.ssafy.stargate.model.entity.PUser;
import com.ssafy.stargate.model.repository.FUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FUserServiceImpl implements FUserService{
    @Autowired
    private FUserRepository fUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public FUserRegisterDto.Response create(FUserRegisterDto.Request dto) throws RegisterException {
        FUser dbCheck = fUserRepository.findById(dto.getEmail()).orElse(null);
        if (dbCheck != null) {
            log.error("회원 회원가입 실패. 가입 데이터 : {}", dto);
            throw new RegisterException();
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        FUser fuser = FUserRegisterDto.dtoToEntity(dto);
        return FUserRegisterDto.entityToDto(fUserRepository.save(fuser));
    }
}
