package com.appointment_scheduling_app.security.repository;

import com.appointment_scheduling_app.security.entity.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OurUserRepository extends JpaRepository<OurUser, Long> {
    Optional<OurUser> findByEmail(String email);
    @Transactional
    void deleteByUserId(long userId);

    boolean existsByEmail(String email);
}
