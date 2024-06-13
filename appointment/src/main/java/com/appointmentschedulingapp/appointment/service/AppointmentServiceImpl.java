package com.appointmentschedulingapp.appointment.service;

import com.appointmentschedulingapp.appointment.dto.AppointmentDTO;
import com.appointmentschedulingapp.appointment.dto.ClientDTO;
import com.appointmentschedulingapp.appointment.dto.StaffDTO;
import com.appointmentschedulingapp.appointment.entity.Appointment;
import com.appointmentschedulingapp.appointment.enumeration.AppointmentStatus;
import com.appointmentschedulingapp.appointment.enumeration.AppointmentType;
import com.appointmentschedulingapp.appointment.exception.ResourceNotFoundException;
import com.appointmentschedulingapp.appointment.mail.EmailService;
import com.appointmentschedulingapp.appointment.mapper.AddressMapper;
import com.appointmentschedulingapp.appointment.mapper.AppointmentMapper;
import com.appointmentschedulingapp.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public long createAppointment(AppointmentDTO appointmentDTO) {
        long clientId = appointmentDTO.getClientId();
        long staffId = appointmentDTO.getAssignedStaffId();

        WebClient client1 = webClientBuilder.baseUrl("http://client").build();
        String uri1 = "/client/fetchClientByClientId?clientId={clientId}";
        ClientDTO clientDTO = client1.get()
                .uri(uri1, clientId)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();

        WebClient client2 = webClientBuilder.baseUrl("http://staff").build();
        String uri2 = "/staff/fetchStaffByStaffId?staffId={staffId}";
        StaffDTO staffDTO = client2.get()
                .uri(uri2, staffId)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();

        if (clientDTO == null) {
            throw new ResourceNotFoundException("Client", "clientId", clientId);
        } else if (staffDTO == null) {
            throw new ResourceNotFoundException("Staff", "staffId", staffId);
        } else {
            Appointment appointment = AppointmentMapper.mapToAppointment(appointmentDTO);
            Long appointmentNumber = createAppointmentNumber();
            appointment.setAppointmentNumber(appointmentNumber);
            appointment.setCreatedAt(LocalDateTime.now());
            Appointment savedAppointment = appointmentRepository.save(appointment);
            AppointmentDTO savedAppointmentDTO = AppointmentMapper.mapToAppointmentDTO(savedAppointment);

            String clientEmail = clientDTO.getEmail();
            String staffEmail = staffDTO.getEmail();

            String message1 = String.format("Email: [%s], Your appointment has been scheduled. Please check the details:\n%s", clientEmail, savedAppointmentDTO);
            kafkaTemplate.send("appointment-creation-client", clientEmail, message1);

            String message2 = String.format("Email: [%s], You have been scheduled for the appointment. Please check the details:\n%s", staffEmail, savedAppointmentDTO);
            kafkaTemplate.send("appointment-creation-staff", staffEmail, message2);

            return savedAppointmentDTO.getAppointmentNumber();
        }
    }

    @Override
    public AppointmentDTO fetchAppointment(long appointmentNumber) {
        Appointment appointment = appointmentRepository.findByAppointmentNumber(appointmentNumber).orElseThrow(
                () -> new ResourceNotFoundException("Appointment", "appointmentNumber", appointmentNumber)
        );
        return AppointmentMapper.mapToAppointmentDTO(appointment);
    }

    @Override
    @Transactional
    public boolean updateAppointment(AppointmentDTO appointmentDTO) {
        long appointmentNumber = appointmentDTO.getAppointmentNumber();
        Appointment appointment = appointmentRepository.findByAppointmentNumber(appointmentNumber).orElseThrow(
                () -> new ResourceNotFoundException("Appointment", "AppointmentNumber", appointmentNumber)
        );

        long clientId = appointmentDTO.getClientId();
        long staffId = appointmentDTO.getAssignedStaffId();

        WebClient client1 = webClientBuilder.baseUrl("http://client").build();
        String uri1 = "/client/fetchClientByClientId?clientId={clientId}";
        ClientDTO clientDTO = client1.get()
                .uri(uri1, clientId)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();

        WebClient client2 = webClientBuilder.baseUrl("http://staff").build();
        String uri2 = "/staff/fetchStaffByStaffId?staffId={staffId}";
        StaffDTO staffDTO = client2.get()
                .uri(uri2, staffId)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();

        appointment.setAppointmentType(appointmentDTO.getAppointmentType());
        appointment.setAssignedStaffId(appointmentDTO.getAssignedStaffId());
        appointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
        appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        appointment.setAppointmentAddress(AddressMapper.mapToAddress(appointmentDTO.getAppointmentAddressDTO()));
        appointment.setUpdatedAt(LocalDateTime.now());
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        String message1 = String.format("Your appointment has been updated. Please check the updated details:\n%s", appointmentDTO);
        emailService.sendEmail(clientDTO.getEmail(), "Appointment Update Notification", message1);

        String message2 = String.format("An appointment assigned to you has been updated. Please check the updated details:\n%s", appointmentDTO);
        emailService.sendEmail(staffDTO.getEmail(), "Appointment Update Notification", message2);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteByAppointmentNumber(long appointmentNumber) {
        Appointment appointment = appointmentRepository.findByAppointmentNumber(appointmentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "AppointmentNumber", appointmentNumber));
        Appointment deletedAppointment = appointment;
        AppointmentDTO deletedAppointmentDTO = AppointmentMapper.mapToAppointmentDTO(deletedAppointment);
        appointmentRepository.delete(appointment);

        long clientId = appointment.getClientId();
        long staffId = appointment.getAssignedStaffId();

        WebClient client1 = webClientBuilder.baseUrl("http://client").build();
        String uri1 = "/client/fetchClientByClientId?clientId={clientId}";
        ClientDTO clientDTO = client1.get()
                .uri(uri1, clientId)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();

        WebClient client2 = webClientBuilder.baseUrl("http://staff").build();
        String uri2 = "/staff/fetchStaffByStaffId?staffId={staffId}";
        StaffDTO staffDTO = client2.get()
                .uri(uri2, staffId)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();

        if (clientDTO == null) {
            throw new ResourceNotFoundException("Client", "clientId", clientId);
        } else if (staffDTO == null) {
            throw new ResourceNotFoundException("Staff", "staffId", staffId);
        } else {
            String clientEmail = clientDTO.getEmail();
            String staffEmail = staffDTO.getEmail();

            String message1 = String.format("Email: [%s], Your appointment has been deleted. Please check the details of the DELETED appointment:\n%s", clientEmail, deletedAppointmentDTO);
            kafkaTemplate.send("appointment-deletion-client", clientEmail, message1);

            String message2 = String.format("Email: [%s], Appointment you were assigned has been deleted. Please check the details of the DELETED appointment:\n%s", staffEmail, deletedAppointmentDTO);
            kafkaTemplate.send("appointment-deletion-staff", staffEmail, message2);
        }
        return true;
    }

    @Override
    public Optional<Appointment> findByAppointmentNumber(long appointmentNumber) {
        return appointmentRepository.findByAppointmentNumber(appointmentNumber);
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByClientId(long clientId) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getClientId() != null && appointment.getClientId() == clientId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByStaffId(long staffId) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAssignedStaffId() != null && appointment.getAssignedStaffId() == staffId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AppointmentDTO> fetchAppointmentsByClientEmail(String email) {
        WebClient client = webClientBuilder.baseUrl("http://security/client").build();
        String uri = "/client/fetchClientByEmail?email={email}";
        ClientDTO clientDTO = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();

        if (clientDTO == null) {
            throw new RuntimeException("Client not found");
        }

        long clientId = clientDTO.getClientId();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getClientId() != null && appointment.getClientId() == clientId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<AppointmentDTO> fetchAppointmentsByStaffEmail(String email) {
        WebClient client = webClientBuilder.baseUrl("http://security/staff").build();
        String uri = "/staff/fetchStaffByEmail?email={email}";
        StaffDTO staffDTO = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();

        if (staffDTO == null) {
            throw new RuntimeException("Client not found");
        }

        long staffId = staffDTO.getStaffId();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAssignedStaffId() != null && appointment.getAssignedStaffId() == staffId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByClientPhoneNumber(String phoneNumber) {
        WebClient client = webClientBuilder.baseUrl("http://security/client").build();
        String uri = "/client/fetchClientByPhoneNumber?phoneNumber={phoneNumber}";
        ClientDTO clientDTO = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();

        if (clientDTO == null) {
            throw new RuntimeException("Client not found");
        }

        long clientId = clientDTO.getClientId();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getClientId() != null && appointment.getClientId() == clientId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByStaffPhoneNumber(String phoneNumber) {
        WebClient client = webClientBuilder.baseUrl("http://security/staff").build();
        String uri = "/staff/fetchStaffByPhoneNumber?phoneNumber={phoneNumber}";
        StaffDTO staffDTO = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();

        if (staffDTO == null) {
            throw new RuntimeException("Staff not found");
        }

        long staffId = staffDTO.getStaffId();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAssignedStaffId() != null && appointment.getAssignedStaffId() == staffId)
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    private Long createAppointmentNumber() {
        while (true) {
            long randomNumber = 1000000000L + new Random().nextInt(900000000);
            if (isUnique(randomNumber)) {
                return randomNumber;
            }
        }
    }

    private boolean isUnique(long randomNumber) {
        return findByAppointmentNumber(randomNumber).isEmpty();
    }

    @Override
    public List<String> fetchAllAppointmentStatus() {
        return Arrays.stream(AppointmentStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> fetchAllAppointmentType() {
        return Arrays.stream(AppointmentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String fetchClientNameByClientId(long clientId) {
        WebClient client = webClientBuilder.baseUrl("http://security/client").build();
        String uri = "/client/fetchClientNameByClientId?clientId={clientId}";
        String name = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (name == null) {
            throw new RuntimeException("Client not found");
        }

        return clientId + " - " + name;
    }

    @Override
    @Transactional
    public String fetchStaffNameByStaffId(long staffId) {
        WebClient client = webClientBuilder.baseUrl("http://security/client").build();
        String uri = "/staff/fetchStaffNameByStaffId?staffId={staffId}";
        String name = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (name == null) {
            throw new RuntimeException("Staff not found");
        }

        return staffId + " - " + name;
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsBySingleDate(LocalDate date) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null && appointment.getAppointmentDateTime().toLocalDate().isEqual(date))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByDoubleDates(LocalDate date1, LocalDate date2) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> {
                    if (appointment.getAppointmentDateTime() == null) {
                        return false;
                    }
                    LocalDate appointmentDate = appointment.getAppointmentDateTime().toLocalDate();
                    return (appointmentDate.isEqual(date1) || appointmentDate.isAfter(date1)) &&
                            (appointmentDate.isEqual(date2) || appointmentDate.isBefore(date2));
                })
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentStatus(String appointmentStatus) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentStatus() == AppointmentStatus.valueOf(appointmentStatus))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentType(String appointmentType) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentType() == AppointmentType.valueOf(appointmentType))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsForToday() {
        LocalDate today = LocalDate.now();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null && appointment.getAppointmentDateTime().toLocalDate().isEqual(today))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByStatusForToday(String appointmentStatus) {
        LocalDate today = LocalDate.now();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null && appointment.getAppointmentDateTime().toLocalDate().isEqual(today) &&
                        appointment.getAppointmentStatus() == AppointmentStatus.valueOf(appointmentStatus))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByTypeForToday(String appointmentType) {
        LocalDate today = LocalDate.now();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null && appointment.getAppointmentDateTime().toLocalDate().isEqual(today) &&
                        appointment.getAppointmentType() == AppointmentType.valueOf(appointmentType))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByStatusAndTypeForToday(String appointmentStatus, String appointmentType) {
        LocalDate today = LocalDate.now();
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null && appointment.getAppointmentDateTime().toLocalDate().isEqual(today) &&
                        appointment.getAppointmentStatus() == AppointmentStatus.valueOf(appointmentStatus) &&
                        appointment.getAppointmentType() == AppointmentType.valueOf(appointmentType))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentTypeAndSingleDate(String appointmentType, LocalDate date) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null &&
                        appointment.getAppointmentType() == AppointmentType.valueOf(appointmentType) &&
                        appointment.getAppointmentDateTime().toLocalDate().isEqual(date))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentTypeAndDoubleDates(String appointmentType, LocalDate date1, LocalDate date2) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null &&
                        appointment.getAppointmentType() == AppointmentType.valueOf(appointmentType) &&
                        (appointment.getAppointmentDateTime().toLocalDate().isEqual(date1) || appointment.getAppointmentDateTime().toLocalDate().isAfter(date1)) &&
                        (appointment.getAppointmentDateTime().toLocalDate().isEqual(date2) || appointment.getAppointmentDateTime().toLocalDate().isBefore(date2)))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentStatusAndSingleDate(String appointmentStatus, LocalDate date) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null &&
                        appointment.getAppointmentStatus() == AppointmentStatus.valueOf(appointmentStatus) &&
                        appointment.getAppointmentDateTime().toLocalDate().isEqual(date))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsByAppointmentStatusAndDoubleDates(String appointmentStatus, LocalDate date1, LocalDate date2) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime() != null &&
                        appointment.getAppointmentStatus() == AppointmentStatus.valueOf(appointmentStatus) &&
                        (appointment.getAppointmentDateTime().toLocalDate().isEqual(date1) || appointment.getAppointmentDateTime().toLocalDate().isAfter(date1)) &&
                        (appointment.getAppointmentDateTime().toLocalDate().isEqual(date2) || appointment.getAppointmentDateTime().toLocalDate().isBefore(date2)))
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> fetchAllAppointments(){
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }
}