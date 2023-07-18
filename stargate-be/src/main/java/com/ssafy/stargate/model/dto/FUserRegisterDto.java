package com.ssafy.stargate.model.dto;

import com.ssafy.stargate.model.entity.FUser;
import lombok.*;

import java.time.LocalDateTime;

public class FUserRegisterDto {
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
        private LocalDateTime birthday;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

    }

    public static Response entityToDto(FUser fUser){
        return Response.builder()
                .build();
    }

    public static FUser dtoToEntity(Request dto){
        return FUser.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .birthday(dto.getBirthday())
                .build();
    }
}
