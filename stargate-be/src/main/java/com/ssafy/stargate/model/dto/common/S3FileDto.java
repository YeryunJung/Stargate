package com.ssafy.stargate.model.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class S3FileDto {
    private String originName;
    private String uploadName;
    private String uploadPath;
    private String uploadUrl;
}
