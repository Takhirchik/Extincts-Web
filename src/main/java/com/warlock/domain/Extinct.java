package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "stand_id", nullable = false)
    private Stand stand;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "extinct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExtinctImage> extinctImages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extinct extinct = (Extinct) o;
        return extinctName.equals(extinct.extinctName) && description.equals(extinct.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extinctName, description);
    }

    @Override
    public String toString(){
        return "Extinct{" +
                "extinctName=" + this.extinctName +
                ", description=" + this.description +
                ", stand=" + this.stand +
                "}";
    }
}
