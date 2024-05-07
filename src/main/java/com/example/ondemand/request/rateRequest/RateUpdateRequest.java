package com.example.ondemand.request.rateRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
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
