package com.example.ondemand.dto.availabilityRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


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
