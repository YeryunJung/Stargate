package com.ssafy.stargate.model.dto.response;

import com.ssafy.stargate.model.entity.Meeting;
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
public class MeetingListResponseDto {
    private UUID uuid;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startDate;

    public static List<MeetingListResponseDto> entityToDto(List<Meeting> meetings) {
        return meetings.stream().map((meeting -> MeetingListResponseDto.builder()
                        .uuid(meeting.getUuid())
                        .name(meeting.getName())
                        .startDate(meeting.getStartDate())
                        .build()
                ))
                .toList();
    }
}
