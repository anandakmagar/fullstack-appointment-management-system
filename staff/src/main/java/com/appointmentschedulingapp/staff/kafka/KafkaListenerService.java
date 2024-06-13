package com.appointmentschedulingapp.staff.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "staff-registration", groupId = "appointment-scheduling-group")
    public void listen(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Staff Registration", message);
    }

    @KafkaListener(topics = "staff-update", groupId = "appointment-scheduling-group")
    public void listen2(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Staff Update Notification", message);
    }

    @KafkaListener(topics = "staff-deletion", groupId = "appointment-scheduling-group")
    public void listen3(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Staff Deletion Notification", message);
    }


    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String extractEmailFromMessage(String message) {
        String email = message.substring(message.indexOf("Email: [") + 8, message.indexOf("],"));
        return email;
    }
}
