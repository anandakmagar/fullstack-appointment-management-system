package com.appointmentschedulingapp.client.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAddressDTO {
    @NotEmpty(message = "Facility name field cannot be null or empty")
    private String streetAddress;
    @NotEmpty(message = "Facility name field cannot be null or empty")
    private String city;
    @NotEmpty(message = "Facility name field cannot be null or empty")
    private String state;
    @NotEmpty(message = "Facility name field cannot be null or empty")
    private String postalCode;

    @Override
    public String toString() {
        return String.format("  Street Address: %s\n  City: %s\n  State: %s\n  Postal Code: %s",
                streetAddress, city, state, postalCode);
    }
}

