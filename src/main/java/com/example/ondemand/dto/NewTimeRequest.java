package com.example.ondemand.dto;


import com.example.ondemand.entities.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTimeRequest {
    private LocalTime start;
    private LocalTime end;

    public boolean isStartPresent(){
        return start != null;
    }

    public boolean isEndPresent(){
        return end != null;
    }

}
