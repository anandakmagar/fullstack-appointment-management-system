package com.appointmentschedulingapp.staff.mapper;

import com.appointmentschedulingapp.staff.dto.AppointmentAddressDTO;
import com.appointmentschedulingapp.staff.entity.AppointmentAddress;

public class AppointmentAddressMapper {

    public static AppointmentAddressDTO mapToAppointmentAddressDTO(AppointmentAddress appointmentAddress) {
        AppointmentAddressDTO appointmentAddressDTO = new AppointmentAddressDTO();
        if (appointmentAddress != null) {
            appointmentAddressDTO.setFacilityName(appointmentAddress.getFacilityName());
            appointmentAddressDTO.setStreetAddress(appointmentAddress.getStreetAddress());
            appointmentAddressDTO.setCity(appointmentAddress.getCity());
            appointmentAddressDTO.setState(appointmentAddress.getState());
            appointmentAddressDTO.setPostalCode(appointmentAddress.getPostalCode());
        }
        return appointmentAddressDTO;
    }

    public static AppointmentAddress mapToAppointmentAddress(AppointmentAddressDTO appointmentAddressDTO) {
        AppointmentAddress appointmentAddress = new AppointmentAddress();
        if (appointmentAddressDTO != null) {
            appointmentAddress.setFacilityName(appointmentAddressDTO.getFacilityName());
            appointmentAddress.setStreetAddress(appointmentAddressDTO.getStreetAddress());
            appointmentAddress.setCity(appointmentAddressDTO.getCity());
            appointmentAddress.setState(appointmentAddressDTO.getState());
            appointmentAddress.setPostalCode(appointmentAddressDTO.getPostalCode());
        }
        return appointmentAddress;
    }
}

