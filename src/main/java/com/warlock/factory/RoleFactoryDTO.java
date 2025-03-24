package com.warlock.factory;

import com.warlock.domain.Role;
import com.warlock.model.request.CreateRoleRequest;
import com.warlock.model.response.RoleResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RoleFactoryDTO {

    public Role fromDTOToEntity(CreateRoleRequest request){
        Role role = new Role();
        role.setName(request.getName());
        return role;
    }

    public RoleResponse fromEntityToDTO(Role role){
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        return roleResponse;
    }
}
