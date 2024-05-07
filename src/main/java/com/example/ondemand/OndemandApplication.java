package com.example.ondemand;

import com.example.ondemand.entities.Day;
import com.example.ondemand.entities.Role;
import com.example.ondemand.repositories.DayRepository;
import com.example.ondemand.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
public class OndemandApplication implements CommandLineRunner {


    @Autowired
    DayRepository dayRepository;

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(OndemandApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int i;

        String[] dayNames = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};

        for(i=0;i<6;i++) {
            Day day = new Day();
            day.setDayName(dayNames[i]);
            dayRepository.save(day);
        }

        String[] roles = {"ADMIN","DRIVER","MANAGER"};
        for(i=0;i<3;i++) {
            Role role = new Role();
            role.setName(roles[i]);
            roleRepository.save(role);
        }


    }
}
