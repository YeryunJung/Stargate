package com.ssafy.stargate.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class FUser extends BaseEntity{
    @Id
    @Column(length = 225)
    private String email;

    @Column(length = 225, nullable = false)
    private String name;

    @Column(length = 225)
    private String nickname;

    @Column(length = 225, nullable = false)
    private String password;

    @Column
    private LocalDateTime birthday;
}
