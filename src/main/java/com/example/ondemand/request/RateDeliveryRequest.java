package com.example.ondemand.request;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RateDeliveryRequest {
    private int rate;
    private String comment;

    private LocalDateTime evaluationTime;

    // Constructors, getters, and setters

    public RateDeliveryRequest() {
    }

    public RateDeliveryRequest(int rate, String comment) {
        this.rate = rate;
        this.comment = comment;
    }

    public int getRating() {
        return rate;
    }

    public void setRating(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
