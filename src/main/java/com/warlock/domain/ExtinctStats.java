package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="extincts_stats")
public class ExtinctStats extends BaseDomain {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extinct_id", nullable = false)
    private Extinct extinct;

    @Column(name="date", nullable = false)
    private LocalDate date;

    @Column(name = "views", nullable = false)
    private Integer views = 0;

    @Column(name = "likes", nullable = false)
    private Integer likes = 0;


}
