package com.ssafy.stargate.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PUserRegisterRequestDto {
    private String email;
    private String password;
    private String code;
    private String name;
}
