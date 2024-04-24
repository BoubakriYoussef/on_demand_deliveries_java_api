package com.example.ondemand.service;


import com.example.ondemand.dto.RoleDTO;
import com.example.ondemand.entities.Role;


public interface RoleService {

    Role addRole(RoleDTO roleDTO);

    void deleteRole(Long id);
}
