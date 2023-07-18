package com.ssafy.stargate.controller;

import com.ssafy.stargate.exception.RegisterException;
import com.ssafy.stargate.model.dto.FUserRegisterDto;
import com.ssafy.stargate.model.service.FUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fusers")
@RequiredArgsConstructor
public class FUserController {
    private final FUserService fUserService;
    @PostMapping("/regist")
    // TODO: exception 분리
    public FUserRegisterDto.Response regist(@ModelAttribute FUserRegisterDto.Request dto) throws RegisterException {
        try {
            return fUserService.create(dto);
        } catch(Exception e) {
            throw e;
        }
    }
}
