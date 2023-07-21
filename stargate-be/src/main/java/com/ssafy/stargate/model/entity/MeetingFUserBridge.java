package com.ssafy.stargate.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "meeting_f_user_bridge")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class MeetingFUserBridge extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO) // TODO: vs IDENTITY?
    private long no;


    @ManyToOne
    @JoinColumn(name="f_user.email")
    private FUser fUser;

    @ManyToOne
    @JoinColumn(name="meeting.uuid")
    private Meeting meeting;

    @Column(name = "order_num", nullable = false)
    @ColumnDefault("0")
    private int orderNum;

}
