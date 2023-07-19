package com.ssafy.stargate.model.dto.response;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PGroupResponseDto {
    private long groupNo;
    private String name;
    private List<PMemberResponseDto> members;
}
