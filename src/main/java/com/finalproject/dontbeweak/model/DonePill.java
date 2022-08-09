package com.finalproject.dontbeweak.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class DonePill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "pill_id")
    private Pill pill;

    @Column(nullable = false)
    private Boolean done;

    @Column(nullable = false)
    private String datetime;
}
