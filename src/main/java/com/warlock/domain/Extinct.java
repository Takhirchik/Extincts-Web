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
@Table(name="extincts")
public class Extinct extends BaseDomain {

    @Column(name = "extinct_name", nullable = false)
    private String extinctName;

    @Column(name = "description")
    private String description;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stand_id")
    private Stand stand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "extinct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExtinctImage> extinctImages = new ArrayList<>();

    @OneToMany(mappedBy = "extinct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExtinctStats> extinctStats = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extinct extinct = (Extinct) o;
        return extinctName.equals(extinct.extinctName) && description.equals(extinct.description) &&
                createdAt.equals(extinct.createdAt) && creator.equals(extinct.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extinctName, description, createdAt, creator);
    }

    @Override
    public String toString(){
        return "Extinct{" +
                "extinctName=" + this.extinctName +
                ", description=" + this.description +
                ", views=" + this.views +
                ", likes=" + this.likes +
                ", createdAt=" + this.createdAt +
                ", stand=" + this.stand +
                ", creator=" + this.creator +
                "}";
    }
}
