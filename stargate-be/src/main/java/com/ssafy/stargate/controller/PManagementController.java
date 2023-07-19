package com.ssafy.stargate.controller;

import com.ssafy.stargate.model.service.PManagmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 소속사의 그룹, 멤버 관리를 수행하는 컨트롤러
 */
@RequestMapping("/pmanagements")
@RestController
@Slf4j
public class PManagementController {
    @Autowired
    PManagmentService managmentService;

    @GetMapping("/")
    public ResponseEntity<?> getGroupList(Principal principal){

        return ResponseEntity.ok("살려줘");
    }


}
