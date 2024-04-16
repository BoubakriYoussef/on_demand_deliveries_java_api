package com.example.ondemand;

import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OndemandApplication implements CommandLineRunner{


    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(OndemandApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
            Role role = new Role();
            role.setRoleName("ROLE_USER");
            roleRepository.save(role);
    }
}
