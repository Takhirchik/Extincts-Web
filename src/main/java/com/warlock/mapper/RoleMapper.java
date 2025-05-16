package com.warlock.mapper;

import com.warlock.domain.Role;
import com.warlock.model.request.CreateRoleRequest;
import com.warlock.model.response.RoleResponse;

public interface RoleMapper {
    Role fromRequestToEntity(CreateRoleRequest request);
    RoleResponse fromEntityToResponse(Role role);
}
