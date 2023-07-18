package com.ssafy.stargate.controller;

import com.ssafy.stargate.model.dto.FUserRegisterDTO;
import com.ssafy.stargate.model.service.FUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // TODO[?]: restController vs Controller
@RequestMapping("/fusers")
@RequiredArgsConstructor
public class FUserController {
    private final FUserService fUserService;
    @PostMapping("/regist")
    // TODO: exception 분리
    public ResponseEntity<FUserRegisterDTO.Response> regist(@RequestBody FUserRegisterDTO.Request dto) throws Exception {
        try {
//            RegisterFUserDTO.Response
            fUserService.create(dto);
        } catch(Exception e) {
            throw e;
        }
    }
}
