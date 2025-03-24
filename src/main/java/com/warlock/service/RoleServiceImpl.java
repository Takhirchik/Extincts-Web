package com.warlock.service;

import com.warlock.domain.Role;
import com.warlock.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private final RoleRepository roleRepository;


    @Override
    public @NonNull List<Role> findAll(){
        return roleRepository.findAll();
    }

    @Override
    public @NonNull Role findByName(@NonNull String roleName){
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role " + roleName + " not found"));
    }
}
