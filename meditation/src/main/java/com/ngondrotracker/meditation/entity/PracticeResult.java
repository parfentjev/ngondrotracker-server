package com.ngondrotracker.meditation.entity;

import com.ngondrotracker.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.util.TimeZone;

@Data
@Entity
public class PracticeResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "timezone", nullable = false)
    private TimeZone timeZone;

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "meditation_id", nullable = false)
    private Meditation meditation;
}
