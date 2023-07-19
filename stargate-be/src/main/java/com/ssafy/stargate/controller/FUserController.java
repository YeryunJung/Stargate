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

@RestController
@RequestMapping("/fusers")
@RequiredArgsConstructor
@Slf4j
public class FUserController {
    private final FUserService fUserService;

    @PostMapping("/register")
    public ResponseEntity<FUserRegisterDto.Response> createFUsers(@ModelAttribute FUserRegisterDto.Request dto) throws RegisterException {
        try{
            fUserService.create(dto);
            return ResponseEntity.ok(null);
        }catch (RegisterException e){
            return  ResponseEntity.status(600).build();
        }
    }
}
