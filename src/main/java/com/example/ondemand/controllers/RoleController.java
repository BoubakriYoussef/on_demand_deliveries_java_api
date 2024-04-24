package com.example.ondemand.controllers;


import com.example.ondemand.dto.RoleDTO;
import com.example.ondemand.entities.Role;
import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {


    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/addRole")
    public ResponseEntity<Role> addRole(
            @RequestBody RoleDTO roleDTO
            ){
        return ResponseEntity.ok(roleService.addRole(roleDTO));
    }



    @GetMapping("/allRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
