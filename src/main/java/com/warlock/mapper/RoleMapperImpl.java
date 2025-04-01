package com.warlock.mapper;

import com.warlock.domain.Role;
import com.warlock.model.request.CreateRoleRequest;
import com.warlock.model.response.RoleResponse;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role fromRequestToEntity(CreateRoleRequest request){
        Role role = new Role();
        role.setName(request.getName());
        return role;
    }

    @Override
    public RoleResponse fromEntityToResponse(Role role){
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        return response;
    }
}
