package com.appointmentschedulingapp.appointment.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "appointment-creation-staff", groupId = "appointment-scheduling-group")
    public void listen1(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Appointment Scheduled Notification", message);
    }

    @KafkaListener(topics = "appointment-creation-staff", groupId = "appointment-scheduling-group")
    public void listen2(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Appointment Scheduled Notification", message);
    }

    @KafkaListener(topics = "appointment-deletion-client", groupId = "appointment-scheduling-group")
    public void listen3(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Appointment Deletion Notification", message);
    }

    @KafkaListener(topics = "appointment-deletion-staff", groupId = "appointment-scheduling-group")
    public void listen4(String message) {
        String email = extractEmailFromMessage(message);
        sendEmail(email, "Appointment Deletion Notification", message);
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
