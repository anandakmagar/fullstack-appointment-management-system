package com.appointmentschedulingapp.appointment.dto;

import com.appointmentschedulingapp.appointment.entity.AppointmentAddress;
import com.appointmentschedulingapp.appointment.enumeration.AppointmentStatus;
import com.appointmentschedulingapp.appointment.enumeration.AppointmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDTO {
    private Long appointmentNumber;
    private Long clientId;
    private AppointmentType appointmentType;
    @NotNull(message = "Appointment must be assigned to someone")
    private Long assignedStaffId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus appointmentStatus;
    private AppointmentAddressDTO appointmentAddressDTO;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    @Override
    public String toString() {
        return String.format("Appointment Number: %s\nClient ID: %s\nAppointment Type: %s\nAssigned Staff ID: %s\nAppointment Date Time: %s\nAppointment Status: %s\nCreated At: %s\nUpdated At: %s\nAddress:\n%s",
                appointmentNumber,
                clientId,
                appointmentType,
                assignedStaffId,
                appointmentDateTime,
                appointmentStatus,
                createdAt,
                updatedAt,
                appointmentAddressDTO.toString());
    }


}
