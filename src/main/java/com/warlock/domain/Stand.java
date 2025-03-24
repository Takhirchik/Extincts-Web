package com.warlock.domain;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

import javax.swing.*;
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

    @ManyToOne
    @JoinColumn(name = "stand_category_id", nullable = false)
    private StandCategory standCategory;

    @OneToMany(mappedBy = "stand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Extinct> extincts;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stand stand = (Stand) o;
        return standName.equals(stand.standName) && description.equals(stand.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(standName, description);
    }

    @Override
    public String toString(){
        return "Stand{" +
                "stand_name" + this.standName +
                ", description=" + this.description +
                ", stand_category=" + this.standCategory +
                "}";
    }
}
