package com.ssafy.stargate.model.dto.common;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDto {

    private String roomNo;
    private String email;
    private String message;
}
