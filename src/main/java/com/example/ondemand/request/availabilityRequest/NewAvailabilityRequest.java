package com.example.ondemand.request.availabilityRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
