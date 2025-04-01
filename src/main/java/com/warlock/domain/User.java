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
@Table(name="users")
public class User extends BaseDomain {
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
    private String url_image;

    @Column(name = "small_thumbnail_url", nullable = false)
    private String smallThumbnailUrl;

    @Column(name = "medium_thumbnail_url", nullable = false)
    private String mediumThumbnailUrl;

    @Column(name = "large_thumbnail_url", nullable = false)
    private String largeThumbnailUrl;


    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
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
}