package com.ssafy.stargate.controller;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.FUserRegisterDto;
import com.ssafy.stargate.model.service.FUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 팬 유저에 관한 Controller이다.
 * 회원가입, 로그인을 지원한다.
 */
@RestController
@RequestMapping("/fusers")
@RequiredArgsConstructor
@Slf4j
public class FUserController {
    private final FUserService fUserService;

    /**
     * 팬 유저 회원가입
     * @param dto [FUserRegisterDto.Request] 팬 유저 회원가입 request
     * @return [ResponseEntity<FUserRegisterDto.Response>] 팬 유저 회원가입 response
     * @throws RegisterException 회원가입 등록 실패
     */
    @PostMapping("/register")
    public ResponseEntity<FUserRegisterDto.Response> createFUsers(@ModelAttribute FUserRegisterDto.Request dto) throws RegisterException {
        try{
            return ResponseEntity.ok(fUserService.create(dto));
        }catch (RegisterException e){
            return  ResponseEntity.status(600).build();
        }

    }
}
