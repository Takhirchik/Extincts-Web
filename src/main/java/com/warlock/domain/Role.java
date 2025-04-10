package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="roles")
public class Role extends BaseDomain implements GrantedAuthority {

    @Column(name="name", nullable=false, unique=true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return "Role{" +
                "name=" + this.name +
                "}";
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}