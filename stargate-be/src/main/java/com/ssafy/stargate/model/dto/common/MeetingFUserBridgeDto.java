package com.ssafy.stargate.model.dto.common;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingFUserBridgeDto {
    private long no;
    private String email; // FUser
    private int orderNum;
}
