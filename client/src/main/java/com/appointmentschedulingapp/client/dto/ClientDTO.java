package com.appointmentschedulingapp.client.dto;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class ClientDTO {
    private Long clientId;
    private String name;
    private String email;
    private String phoneNumber;
    private ClientAddressDTO clientAddressDTO;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    @Override
    public String toString() {
        return String.format("Name: %s\nEmail: %s\nPhone: %s\nAddress:\n%s",
                name, email, phoneNumber, clientAddressDTO.toString());
    }
}
