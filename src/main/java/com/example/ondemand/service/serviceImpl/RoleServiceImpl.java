package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.dto.RoleRequest;
import com.example.ondemand.entities.Role;
import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@ComponentScan
@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    RoleRepository roleRepository;


    @Override
    public Role addRole(RoleRequest roleRequest) {
        var role = Role.builder()
                .name(roleRequest.getName())
                .build();
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }


}
