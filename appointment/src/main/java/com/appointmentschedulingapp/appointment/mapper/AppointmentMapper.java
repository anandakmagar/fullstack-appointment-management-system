package com.appointmentschedulingapp.appointment.mapper;

import com.appointmentschedulingapp.appointment.dto.AppointmentDTO;
import com.appointmentschedulingapp.appointment.entity.Appointment;

public class AppointmentMapper {
    public static AppointmentDTO mapToAppointmentDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentNumber(appointment.getAppointmentNumber());
        appointmentDTO.setClientId(appointment.getClientId());
        appointmentDTO.setAppointmentType(appointment.getAppointmentType());
        appointmentDTO.setAssignedStaffId(appointment.getAssignedStaffId());
        appointmentDTO.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentDTO.setAppointmentDateTime(appointment.getAppointmentDateTime());
        appointmentDTO.setAppointmentAddressDTO(AddressMapper.mapToAddressDTO(appointment.getAppointmentAddress()));
        appointmentDTO.setCreatedAt(appointment.getCreatedAt());
        appointmentDTO.setUpdatedAt(appointment.getUpdatedAt());

        return appointmentDTO;
    }

    public static Appointment mapToAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setClientId(appointmentDTO.getClientId());
        appointment.setAppointmentType(appointmentDTO.getAppointmentType());
        appointment.setAssignedStaffId(appointmentDTO.getAssignedStaffId());
        appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        appointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
        appointment.setAppointmentAddress(AddressMapper.mapToAddress(appointmentDTO.getAppointmentAddressDTO()));

        return appointment;
    }
}