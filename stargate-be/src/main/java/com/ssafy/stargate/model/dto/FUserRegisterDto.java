package com.ssafy.stargate.model.dto;

import com.ssafy.stargate.model.entity.FUser;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FUserRegisterDto {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Request {
        private String email;
        private String name;
        private String nickname;
        private String password;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private String birthday;
    }

    @Builder
    @Getter
    @Setter
//    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
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
                .birthday(LocalDateTime.parse(dto.getBirthday() + "T00:00:00"))
                .build();
    }
}
