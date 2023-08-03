package com.ssafy.stargate.model.dto.response;

import com.ssafy.stargate.model.dto.common.MeetingDto;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.*;

/**
 * 대시보드 dto
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardResponseDto {

    List<DashboardMeetingResponseDto> today;
    List<DashboardMeetingResponseDto> future;
    List<DashboardMeetingResponseDto> past;
}
