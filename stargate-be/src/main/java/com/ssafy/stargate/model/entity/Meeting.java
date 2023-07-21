package com.ssafy.stargate.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meeting")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "waiting_time", nullable = false)
    @ColumnDefault("10")
    private int waitingTime;

    @Column(name = "meeting_time", nullable = false)
    @ColumnDefault("120")
    private int meetingTime;

    @Column
    private String notice;

    @Column
    private String image;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MeetingMemberBridge> meetingMembers = new ArrayList<>();


    @OneToMany(mappedBy = "meeting", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MeetingFUserBridge> meetingFUsers = new ArrayList<>();
}
