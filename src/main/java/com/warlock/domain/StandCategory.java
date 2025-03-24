package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="stand_categories")
public class StandCategory extends BaseDomain {

    @Column(name="category_name", nullable=false, unique=true)
    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandCategory standCategory = (StandCategory) o;
        return categoryName.equals(standCategory.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

    @Override
    public String toString(){
        return "StandCategory{" +
                "category_name=" + this.categoryName +
                "}";
    }
}
