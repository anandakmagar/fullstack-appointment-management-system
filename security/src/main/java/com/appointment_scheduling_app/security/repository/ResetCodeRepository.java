package com.appointment_scheduling_app.security.repository;

import com.appointment_scheduling_app.security.entity.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCode, Long> {
    ResetCode findByEmail(String email);
}
