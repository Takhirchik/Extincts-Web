package com.warlock.service;

import com.warlock.domain.Role;
import lombok.NonNull;

import java.util.List;

public interface RoleService {

    @NonNull
    List<Role> findAll();

    @NonNull
    Role findByName(@NonNull String roleName);
}
