package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="stands")
public class Stand extends BaseDomain {

    @Column(name = "stand_name", nullable = false)
    private String standName;

    @Column(name = "description")
    private String description;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @OneToMany(mappedBy = "stand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Extinct> extincts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "stand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StandStats> standStats = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stand stand = (Stand) o;
        return standName.equals(stand.standName) && description.equals(stand.description) &&
                createdAt.equals(stand.createdAt) && creator.equals(stand.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(standName, description, createdAt, creator);
    }

    @Override
    public String toString(){
        return "Stand{" +
                "stand_name" + this.standName +
                ", description=" + this.description +
                ", views=" + this.views +
                ", createdAt=" + this.createdAt +
                ", creator=" + this.creator +
                "}";
    }
}
