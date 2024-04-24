package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.dto.RoleDTO;
import com.example.ondemand.entities.Role;
import com.example.ondemand.entities.User;
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
    public Role addRole(RoleDTO roleDTO) {
        var role = Role.builder()
                .name(roleDTO.getName())
                .build();
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }


}
