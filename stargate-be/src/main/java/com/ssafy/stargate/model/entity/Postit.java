package com.ssafy.stargate.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Postit extends BaseEntity{
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long no;

    @Column(name = "content")
    private String contents;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private FUser fUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberNo", referencedColumnName = "member_no")
    private PMember pMember;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uuid", referencedColumnName = "uuid")
    private Meeting meeting;

}
