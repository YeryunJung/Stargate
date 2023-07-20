package com.ssafy.stargate.model.dto.common;

import com.ssafy.stargate.model.entity.MeetingFUserBridge;
import com.ssafy.stargate.model.entity.MeetingMemberBridge;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingDto {
    private UUID uuid;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startDate;
    private int waitingTime;
    private int meetingTime;
    private String notice;
    private String image;
    private List<MeetingMemberBridge> members;
    private List<MeetingFUserBridge> fUsers;
}
