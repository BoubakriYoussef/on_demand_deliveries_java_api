package com.example.ondemand.request.rateRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateUpdateRequest {
    private double rating;
    private String commentary;

    public boolean isRatingPresent() {
        return rating != 0;
    }

    public boolean isCommentatyPresent() {
        return commentary != null;
    }
}
