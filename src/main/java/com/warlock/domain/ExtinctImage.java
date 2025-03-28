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
@Table(name="extincts_images")
public class ExtinctImage extends BaseDomain {

    @Column(name = "url_image", nullable = false)
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extinct_id", nullable = false)
    private Extinct extinct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtinctImage extinctImage = (ExtinctImage) o;
        return urlImage.equals(extinctImage.urlImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlImage);
    }

    @Override
    public String toString(){
        return "ExtinctImage{" +
                "url_image=" + this.urlImage +
                "}";
    }
}