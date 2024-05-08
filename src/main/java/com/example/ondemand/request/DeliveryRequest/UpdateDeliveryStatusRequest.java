package com.example.ondemand.request.DeliveryRequest;

import com.example.ondemand.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateDeliveryStatusRequest {

    private Status status;
}
