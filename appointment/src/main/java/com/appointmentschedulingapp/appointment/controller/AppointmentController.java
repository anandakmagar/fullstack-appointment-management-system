package com.appointmentschedulingapp.appointment.controller;

import com.appointmentschedulingapp.appointment.dto.AppointmentDTO;
import com.appointmentschedulingapp.appointment.dto.CreateAppointmentResponseDTO;
import com.appointmentschedulingapp.appointment.dto.ResponseDTO;
import com.appointmentschedulingapp.appointment.service.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private IAppointmentService iAppointmentService;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @PostMapping("/create")
    public ResponseEntity<CreateAppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO){
        long appointmentNumber = iAppointmentService.createAppointment(appointmentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateAppointmentResponseDTO("201", "Appointment created successfully and your appointment number is given below", appointmentNumber));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteByAppointmentNumber(@RequestParam long appointmentNumber){
        boolean isDeleted = iAppointmentService.deleteByAppointmentNumber(appointmentNumber);
        if (isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("200", "Appointment deleted successfully"));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "An error occurred! Please try again later."));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> update(@RequestBody AppointmentDTO appointmentDTO){
        boolean isUpdated = iAppointmentService.updateAppointment(appointmentDTO);
        if (isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("200", "Appointment updated successfully"));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "An error occurred! Please try again later."));
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<AppointmentDTO> fetchAppointment(@RequestParam long appointmentNumber){
        AppointmentDTO appointmentDTO = iAppointmentService.fetchAppointment(appointmentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTO);
    }

    @GetMapping("/fetchByClientId")
    public ResponseEntity<List<AppointmentDTO>> fetchByClientId(@RequestParam long clientId){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByClientId(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchByStaffId")
    public ResponseEntity<List<AppointmentDTO>> fetchByStaffId(@RequestParam long staffId){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByStaffId(staffId);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchByClientEmail")
    public ResponseEntity<List<AppointmentDTO>> fetchByClientEmail(@RequestParam String email){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByClientEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchByClientPhoneNumber")
    public ResponseEntity<List<AppointmentDTO>> fetchByClientPhoneNumber(@RequestParam String phoneNumber){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByClientPhoneNumber(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchByStaffEmail")
    public ResponseEntity<List<AppointmentDTO>> fetchByStaffEmail(@RequestParam String email){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByStaffEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchByStaffPhoneNumber")
    public ResponseEntity<List<AppointmentDTO>> fetchByStaffPhoneNumber(@RequestParam String phoneNumber){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByStaffPhoneNumber(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchAllAppointmentStatus")
    public List<String> fetchAllAppointmentStatus(){
        return iAppointmentService.fetchAllAppointmentStatus();
    }

    @GetMapping("/fetchAllAppointmentType")
    public List<String> fetchAllAppointmentType(){
        return iAppointmentService.fetchAllAppointmentType();
    }

    @GetMapping("/fetchClientNameByClientId")
    public String fetchClientNameByClientId(long clientId){
        return iAppointmentService.fetchClientNameByClientId(clientId);
    }

    @GetMapping("/fetchStaffNameByStaffId")
    public String fetchStaffNameByStaffId(long staffId){
        return iAppointmentService.fetchStaffNameByStaffId(staffId);
    }

    @GetMapping("/fetchAppointmentsBySingleDate")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsBySingleDate(@RequestParam LocalDate date){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsBySingleDate(date);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchAppointmentsByDoubleDates")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByDoubleDates(@RequestParam LocalDate date1, @RequestParam LocalDate date2) {
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByDoubleDates(date1, date2);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchAppointmentsByAppointmentStatus")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentStatus(@RequestParam String appointmentStatus){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentStatus(appointmentStatus);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByAppointmentType")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentType(@RequestParam String appointmentType){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentType(appointmentType);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsForToday")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsForToday(){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsForToday();
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByAppointmentTypeAndSingleDate")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentTypeAndSingleDate(@RequestParam String appointmentType, @RequestParam LocalDate date){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentTypeAndSingleDate(appointmentType, date);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByAppointmentTypeAndDoubleDates")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentTypeAndDoubleDates(@RequestParam String appointmentType, @RequestParam LocalDate date1, @RequestParam LocalDate date2){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentTypeAndDoubleDates(appointmentType, date1, date2);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByAppointmentStatusAndSingleDate")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentStatusAndSingleDate(@RequestParam String appointmentStatus, @RequestParam LocalDate date){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentStatusAndSingleDate(appointmentStatus, date);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByAppointmentStatusAndDoubleDates")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByAppointmentStatusAndDoubleDates(@RequestParam String appointmentStatus, @RequestParam LocalDate date1, @RequestParam LocalDate date2){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByAppointmentStatusAndDoubleDates(appointmentStatus, date1, date2);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByStatusForToday")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByStatusForToday(@RequestParam String appointmentStatus){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByStatusForToday(appointmentStatus);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByTypeForToday")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByTypeForToday(@RequestParam String appointmentType){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByTypeForToday(appointmentType);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
    @GetMapping("/fetchAppointmentsByStatusAndTypeForToday")
    public ResponseEntity<List<AppointmentDTO>> fetchAppointmentsByStatusAndTypeForToday(@RequestParam String appointmentStatus, @RequestParam String appointmentType){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAppointmentsByStatusAndTypeForToday(appointmentStatus, appointmentType);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }

    @GetMapping("/fetchAllAppointments")
    public ResponseEntity<List<AppointmentDTO>> fetchAllAppointments(){
        List<AppointmentDTO> appointmentDTOs = iAppointmentService.fetchAllAppointments();
        return ResponseEntity.status(HttpStatus.OK).body(appointmentDTOs);
    }
}