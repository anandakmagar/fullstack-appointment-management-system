package com.appointmentschedulingapp.appointment.mapper;

import com.appointmentschedulingapp.appointment.dto.AppointmentAddressDTO;
import com.appointmentschedulingapp.appointment.entity.AppointmentAddress;

public class AddressMapper {
    public static AppointmentAddressDTO mapToAddressDTO(AppointmentAddress appointmentAddress) {
        AppointmentAddressDTO addressDTO = new AppointmentAddressDTO();
        addressDTO.setFacilityName(appointmentAddress.getFacilityName());
        addressDTO.setStreetAddress(appointmentAddress.getStreetAddress());
        addressDTO.setCity(appointmentAddress.getCity());
        addressDTO.setState(appointmentAddress.getState());
        addressDTO.setPostalCode(appointmentAddress.getPostalCode());
        return addressDTO;
    }

    public static AppointmentAddress mapToAddress(AppointmentAddressDTO appointmentAddressDTO) {
        AppointmentAddress address = new AppointmentAddress();
        address.setFacilityName(appointmentAddressDTO.getFacilityName());
        address.setStreetAddress(appointmentAddressDTO.getStreetAddress());
        address.setCity(appointmentAddressDTO.getCity());
        address.setState(appointmentAddressDTO.getState());
        address.setPostalCode(appointmentAddressDTO.getPostalCode());
        return address;
    }
}
