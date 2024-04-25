package com.example.ondemand.service;


import com.example.ondemand.dto.RoleRequest;
import com.example.ondemand.entities.Role;


public interface RoleService {

    Role addRole(RoleRequest roleRequest);

    void deleteRole(Long id);
}
