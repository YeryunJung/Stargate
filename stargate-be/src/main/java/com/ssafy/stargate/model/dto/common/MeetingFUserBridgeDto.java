package com.ssafy.stargate.model.dto.common;

import com.ssafy.stargate.model.entity.FUser;
import com.ssafy.stargate.model.entity.Meeting;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingFUserBridgeDto {
    private long no;
    private FUser fUser;
    private Meeting meeting;
    private int orderNum;
}
