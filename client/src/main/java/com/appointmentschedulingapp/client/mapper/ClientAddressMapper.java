package com.appointmentschedulingapp.client.mapper;

import com.appointmentschedulingapp.client.dto.ClientAddressDTO;
import com.appointmentschedulingapp.client.entity.ClientAddress;

public class ClientAddressMapper {
    public static ClientAddressDTO mapToAddressDTO(ClientAddress clientAddress) {
        ClientAddressDTO clientAddressDTO = new ClientAddressDTO();
        clientAddressDTO.setStreetAddress(clientAddress.getStreetAddress());
        clientAddressDTO.setCity(clientAddress.getCity());
        clientAddressDTO.setState(clientAddress.getState());
        clientAddressDTO.setPostalCode(clientAddress.getPostalCode());
        return clientAddressDTO;
    }

    public static ClientAddress mapToAddress(ClientAddressDTO clientAddressDTO) {
        ClientAddress clientAddress = new ClientAddress();
        clientAddress.setStreetAddress(clientAddressDTO.getStreetAddress());
        clientAddress.setCity(clientAddressDTO.getCity());
        clientAddress.setState(clientAddressDTO.getState());
        clientAddress.setPostalCode(clientAddressDTO.getPostalCode());
        return clientAddress;
    }

}