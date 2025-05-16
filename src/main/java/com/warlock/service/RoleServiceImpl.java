package com.warlock.service;

import com.warlock.domain.Role;
import com.warlock.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "roles")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    @Cacheable(key = "'all'")
    @Override
    public @NonNull List<Role> findAll(){
        return roleRepository.findAll();
    }

    @Cacheable(key = "#roleName")
    @Override
    public @NonNull Role findByName(@NonNull String roleName){
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role " + roleName + " not found"));
    }
}
