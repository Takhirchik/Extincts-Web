package com.warlock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warlock.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseDomain implements UserDetails {
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "bio")
    private String bio;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "avatar_url")
    private String urlImage;

    @Column(name = "small_thumbnail_url")
    private String smallThumbnailUrl;

    @Column(name = "medium_thumbnail_url")
    private String mediumThumbnailUrl;

    @Column(name = "large_thumbnail_url")
    private String largeThumbnailUrl;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return nickname.equals(user.nickname) && login.equals(user.login) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, login, email);
    }

    @Override
    public String toString(){
        return "User{" +
                "nickname=" + this.nickname +
                ", login=" + this.login +
                ", password=" + this.password +
                ", email=" + this.email +
                ", role=" + this.role +
                "}";
    }

    @JsonIgnore
    @Override
    public String getUsername(){
        return this.login;
    }
}