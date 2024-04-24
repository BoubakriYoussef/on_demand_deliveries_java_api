package com.example.ondemand.dto;

import com.example.ondemand.entities.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAvailabilityRequest {
    private List<String> dayNames;
    private List<NewTimeRequest> times;

    public boolean isDayNamesPresent(){
        return dayNames != null;
    }
    public boolean isTimePresent(){
        return times != null;
    }

}
