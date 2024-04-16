package com.example.ondemand;

import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.entities.Role;
import com.example.ondemand.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OndemandApplication implements CommandLineRunner{


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(OndemandApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
            Role role = new Role();
            role.setRoleName("ROLE_USER");
            roleRepository.save(role);

            User user = new User();
            user.setFirstName("Youssef");
            user.setLastName("Boubakri");
            user.setEmail("youssefboubakri@gmail.com");
            user.setPassword(passwordEncoder.encode("12345678"));
            user.setPhone("0615099933");
            userRepository.save(user);
    }
}
