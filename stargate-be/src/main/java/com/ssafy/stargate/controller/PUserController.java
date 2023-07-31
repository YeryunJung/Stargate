package com.ssafy.stargate.controller;

import com.ssafy.stargate.exception.EmailDuplicationException;
import com.ssafy.stargate.exception.LoginException;
import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.response.JwtResponseDto;
import com.ssafy.stargate.model.dto.common.PUserDto;
import com.ssafy.stargate.model.service.PUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 소속사 유저 자체와 관련된 모든 연계 컨트롤러
 * 소속사 그룹 및 멤버는 PManagement 하겠다.
 */
@RequestMapping("/pusers")
@RestController
@Slf4j
public class PUserController {

    @Autowired
    PUserService pUserService;

    /**
     * 소속사 유저의 회원가입을 수행한다.
     *
     * @param dto PUserRegisterDto : 소속사 회원가입 정보를 담는다.
     * @return 성공시 200, 실패시 600
     */
    @PostMapping("/register")
    public ResponseEntity<?> createPUser(@ModelAttribute PUserDto dto) throws EmailDuplicationException, RegisterException{
        try {
            pUserService.register(dto);
            return ResponseEntity.ok(null);
        } catch (RegisterException e) {
            return ResponseEntity.status(600).build();
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkPuserEmailExist(@RequestParam("email")String email){
        boolean result = pUserService.checkEmailExist(email);
        return ResponseEntity.ok(null);
    }


    /**
     * 소속사 유저의 로그인 기능을 수행한다.
     *
     * @param dto : PUserRequestDto : email, password 속성 필.
     * @return 로그인 성공시 SimpleDto에 JWT를 담아 보낸다. 실패시 401 코드 반환
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginPUser(@ModelAttribute PUserDto dto) {
        try {
            return ResponseEntity.ok(pUserService.login(dto));
        } catch (LoginException e) {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 회원 탈퇴를 수행한다.
     * 이메일과 비밀번호를 필수로 요구한다.
     * @param dto PUserRequestDto 소속사 유저 정보(이메일, 비밀번호 필)
     * @return 성공시 200
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePUser(@RequestBody PUserDto dto, Principal principal){
        pUserService.deletePUser(dto,principal);
        return ResponseEntity.ok(null);
    }

    /**
     * 소속사 계정 정보를 로딩한다.
     * @param principal 소속사 이메일이 담긴 객체
     * @return 소속사 계정 정보
     */
    @GetMapping("/get")
    public ResponseEntity<PUserDto> getPUserData(Principal principal){
        PUserDto pUserDto = pUserService.getPUserData(principal);
        return ResponseEntity.ok(pUserDto);
    }

    public ResponseEntity<?> updatePUser(@ModelAttribute PUserDto pUserDto){
        int statusCode = pUserService.updatePUser(pUserDto);
        return ResponseEntity.status(statusCode).build();
    }

}
