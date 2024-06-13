package com.appointmentschedulingapp.appointment.repository;

import com.appointmentschedulingapp.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNumber(long appointmentNumber);

    @Transactional
    void deleteByAppointmentNumber(long appointmentNumber);
}
