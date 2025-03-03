package com.warlock.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Objects;
//import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false, unique=true)
    private String name;

//    @OneToMany(mappedBy = "role")
//    private Set<User> users;
//

//    public Role(){
//    }
//
//    public Role(String name){
//        this.name = name;
//    }

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
                "id=" + this.id +
                ", name=" + this.name +
                "}";
    }
}