package com.ssafy.stargate.model.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PMemberResponseDto {
    private long memberNo;
    private String name;
}
