package com.ssafy.stargate.model.dto;

import com.ssafy.stargate.model.entity.FUser;
import lombok.*;

import java.time.LocalDateTime;

public class FUserRegisterDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String name;
        private String nickname;
        private String password;
        private LocalDateTime birthday; // TODO[?]: String으로 받아야할까?
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

    }

    public static Response entityToDTO(FUser fUser){
        //
    }

    default FUser dTOToEntity(Request dto){
        return FUser.builder()
                .email(FUser.getEmail())
                .name(FUser.getName())
                .nickname(FUser.getNickname())
                .password(FUser.getPassword())
                .birthday(FUser.getBirthday())
                .build();
    }
}
