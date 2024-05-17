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
public class OndemandApplication {




    public static void main(String[] args) {
        SpringApplication.run(OndemandApplication.class, args);
    }


}
