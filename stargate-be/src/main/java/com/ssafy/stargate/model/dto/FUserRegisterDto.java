package com.ssafy.stargate.model.dto;

import com.ssafy.stargate.model.entity.FUser;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 팬 유저 회원가입시 사용되는 DTO이다.
 */
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

    /**
     * 팬 유저 Request DTO을 Entity로 바꾼다
     * @param dto [Request] Request DTO
     * @return [FUser] 팬 유저 엔티티
     */
    public static FUser dtoToEntity(Request dto){
        return FUser.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .birthday(LocalDateTime.parse(dto.getBirthday() + "T00:00:00"))
                .build();
    }

    /**
     * 팬 유저 엔티티를 Response DTO로 바꾼다
     * @param fUser [FUser] 변경할 팬 유저 정보
     * @return [Response] Response DTO
     */
    public static Response entityToDto(FUser fUser){
        return Response.builder()
                .build();
    }
}
