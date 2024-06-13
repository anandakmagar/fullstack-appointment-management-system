package com.appointment_scheduling_app.security.controller;

import com.appointment_scheduling_app.security.dto.*;
import com.appointment_scheduling_app.security.exception.UsernameNotFoundException;
import com.appointment_scheduling_app.security.service.ResetCodeService;
import com.appointment_scheduling_app.security.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5502")
@RequestMapping("/auth")
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private ResetCodeService resetCodeService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        boolean isRegistered = userManagementService.register(registerRequestDTO);
        if (isRegistered) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RegisterResponseDTO(200, "User registered successfully"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponseDTO(400, "Registration failed: User already exists or invalid data provided"));
        }
    }

    @PostMapping("/admin/registerAdmin")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(@RequestBody RegisterRequestDTO registerRequestDTO) {
        boolean isRegistered = userManagementService.registerAdmin(registerRequestDTO);
        if (isRegistered) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RegisterResponseDTO(200, "Admin registered successfully"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponseDTO(400, "Registration failed: User already exists or invalid data provided"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = userManagementService.login(loginRequestDTO);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody LoginResponseDTO refreshTokenRequest) {
        LoginResponseDTO response = userManagementService.refreshToken(refreshTokenRequest);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/send-reset-code")
    public ResponseEntity<String> sendPasswordResetCode(@RequestParam String email) {
        boolean result = resetCodeService.sendPasswordResetCode(email);
        if (result) {
            return ResponseEntity.ok("Password reset code sent successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to send password reset code.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            boolean result = resetCodeService.changePassword(resetPasswordDTO);
            if (result) {
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid reset code or email.");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Username/email not found!!");
        }
    }

    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestParam long userId){
        boolean isDeleted = userManagementService.deleteByUserId(userId);
        return true;
    }

    // Staff microservice
    @PostMapping("/staff/admin/create")
    public ResponseEntity<Object> createStaff(@RequestBody Object object) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/create";
        Object object2 = client.post()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @DeleteMapping("/staff/admin/delete")
    public ResponseEntity<Object> deleteByStaffId(@RequestParam long staffId) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/delete?staffId={staffId}";
        Object object = client.delete()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @PutMapping("/staff/admin/update")
    public ResponseEntity<Object> updateStaff(@RequestBody Object object) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/update";
        Object object2 = client.put()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @GetMapping("/staff/admin-staff/fetch")
    public ResponseEntity<Object> fetchStaff(@RequestParam long staffId) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetch?staffId={staffId}";
        Object object = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/staff/admin-staff/fetchStaffByStaffId")
    public ResponseEntity<Object> fetchStaffByStaffId(@RequestParam long staffId) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetch?staffId={staffId}";
        Object object = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/staff/admin-staff/fetchStaffByEmail")
    public ResponseEntity<Object> fetchStaffByEmail(@RequestParam String email) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetchStaffByEmail?email={email}";
        Object object = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/staff/admin-staff/fetchStaffByPhoneNumber")
    public ResponseEntity<Object> fetchStaffByPhoneNumber(@RequestParam String phoneNumber) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetchStaffByPhoneNumber?phoneNumber={phoneNumber}";
        Object object = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/staff/admin-staff/fetchStaffNameByStaffId")
    public ResponseEntity<String> fetchStaffNameByStaffId(@RequestParam long staffId) {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetch?staffId={staffId}";
        String name = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(name);
    }

    @GetMapping("/staff/admin-staff/fetchAllStaff")
    public ResponseEntity<List<Object>> fetchAllStaff() {
        WebClient client = webClientBuilder.baseUrl("http://staff").build();
        String uri = "/staff/fetchAllStaff";
        List<Object> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    // Client microservice
    @PostMapping("/client/admin/create")
    public ResponseEntity<Object> createClient(@Valid @RequestBody Object object) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/create";
        Object object2 = client.post()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @DeleteMapping("/client/admin/delete")
    public ResponseEntity<Object> deleteByClientId(@RequestParam long clientId) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/delete?clientId={clientId}";
        Object object = client.delete()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @PutMapping("/client/admin/update")
    public ResponseEntity<Object> updateClient(@RequestBody Object object) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/update";
        Object object2 = client.put()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @GetMapping("/client/admin-staff/fetch")
    public ResponseEntity<Object> fetchClient(@RequestParam long clientId) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetch?clientId={clientId}";
        Object object = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/client/admin-staff/fetchClientByClientId")
    public ResponseEntity<Object> fetchClientByClientId(@RequestParam long clientId) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetch?clientId={clientId}";
        Object object = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/client/admin-staff/fetchClientByEmail")
    public ResponseEntity<Object> fetchClientByEmail(@RequestParam String email) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetchClientByEmail?email={email}";
        Object object = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/client/admin-staff/fetchClientByPhoneNumber")
    public ResponseEntity<Object> fetchClientByPhoneNumber(@RequestParam String phoneNumber) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetchClientByPhoneNumber?phoneNumber={phoneNumber}";
        Object object = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/client/admin-staff/fetchClientNameByClientId")
    public ResponseEntity<String> fetchClientNameByClientId(@RequestParam long clientId) {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetch?clientId={clientId}";
        String name = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(name);
    }

    @GetMapping("/client/admin-staff/fetchAllClients")
    public ResponseEntity<List<Object>> fetchAllClients() {
        WebClient client = webClientBuilder.baseUrl("http://client").build();
        String uri = "/client/fetchAllClients";
        List<Object> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    // Appointment microservice
    @PostMapping("/appointment/admin/create")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody Object object) {
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/create";
        Object object2 = client.post()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @DeleteMapping("appointment/admin/delete")
    public ResponseEntity<Object> deleteByAppointmentNumber(@RequestParam long appointmentNumber){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/delete?appointmentNumber={appointmentNumber}";
        Object object = client.delete()
                .uri(uri, appointmentNumber)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @PutMapping("/appointment/admin/update")
    public ResponseEntity<Object> updateAppointment(@RequestBody Object object){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/update";
        Object object2 = client.put()
                .uri(uri)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object2);
    }

    @GetMapping("/appointment/admin-staff/fetch")
    public ResponseEntity<Object> fetchAppointment(@RequestParam long appointmentNumber){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetch?appointmentNumber={appointmentNumber}";
        Object object = client.get()
                .uri(uri, appointmentNumber)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(object);
    }

    @GetMapping("/appointment/admin-staff/fetchByClientId")
    public ResponseEntity<List<Object>> fetchByClientId(@RequestParam long clientId){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByClientId?clientId={clientId}";
        List<Object> list = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchByStaffId")
    public ResponseEntity<List<Object>> fetchByStaffId(@RequestParam long staffId){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByStaffId?staffId={staffId}";
        List<Object> list = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchByClientEmail")
    public ResponseEntity<List<Object>> fetchByClientEmail(@RequestParam String email){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByStaffEmail?email={email}";
        List<Object> list = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchByClientPhoneNumber")
    public ResponseEntity<List<Object>> fetchByClientPhoneNumber(@RequestParam String phoneNumber){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByClientPhoneNumber?phoneNumber={phoneNumber}";
        List<Object> list = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchByStaffEmail")
    public ResponseEntity<List<Object>> fetchByStaffEmail(@RequestParam String email){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByStaffEmail?email={email}";
        List<Object> list = client.get()
                .uri(uri, email)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchByStaffPhoneNumber")
    public ResponseEntity<List<Object>> fetchByStaffPhoneNumber(@RequestParam String phoneNumber){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchByStaffPhoneNumber?phoneNumber={phoneNumber}";
        List<Object> list = client.get()
                .uri(uri, phoneNumber)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchAllAppointmentStatus")
    public List<String> fetchAllAppointmentStatus(){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAllAppointmentStatus";
        List<String> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        return list;
    }

    @GetMapping("/appointment/admin-staff/fetchAllAppointmentType")
    public List<String> fetchAllAppointmentType(){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAllAppointmentType";
        List<String> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        return list;
    }

    @GetMapping("/appointment/admin-staff/fetchClientNameByClientId")
    public String fetchClientNameByClientIdAppointment(long clientId){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchClientNameByClientId?clientId={clientId}";
        String name = client.get()
                .uri(uri, clientId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return name;
    }

    @GetMapping("/appointment/admin-staff/fetchStaffNameByStaffId")
    public String fetchStaffNameByStaffIdAppointment(long staffId){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchStaffNameByStaffId?staffId={staffId}";
        String name = client.get()
                .uri(uri, staffId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return name;
    }

    @GetMapping("/appointment/admin-staff/fetchAppointmentsBySingleDate")
    public ResponseEntity<List<Object>> fetchAppointmentsBySingleDate(@RequestParam LocalDate date){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsBySingleDate?date={date}";
        List<Object> list = client.get()
                .uri(uri, date)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchAppointmentsByDoubleDates")
    public ResponseEntity<List<Object>> fetchAppointmentsByDoubleDates(@RequestParam LocalDate date1, @RequestParam LocalDate date2) {
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByDoubleDates?date1={date1}&date2={date2}";
        List<Object> list = client.get()
                .uri(uri, date1, date2)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentStatus")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentStatus(@RequestParam String appointmentStatus){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentStatus?appointmentStatus={appointmentStatus}";
        List<Object> list = client.get()
                .uri(uri, appointmentStatus)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentType")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentType(@RequestParam String appointmentType){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentType?appointmentType={appointmentType}";
        List<Object> list = client.get()
                .uri(uri, appointmentType)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsForToday")
    public ResponseEntity<List<Object>> fetchAppointmentsForToday(){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsForToday";
        List<Object> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentTypeAndSingleDate")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentTypeAndSingleDate(@RequestParam String appointmentType, @RequestParam LocalDate date){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentTypeAndSingleDate?appointmentType={appointmentType}&date={date}";
        List<Object> list = client.get()
                .uri(uri, appointmentType, date)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentTypeAndDoubleDates")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentTypeAndDoubleDates(@RequestParam String appointmentType, @RequestParam LocalDate date1, @RequestParam LocalDate date2){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentTypeAndDoubleDates?appointmentType={appointmentType}&date1={date1}&date2{date2}";
        List<Object> list = client.get()
                .uri(uri, appointmentType, date1, date2)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentStatusAndSingleDate")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentStatusAndSingleDate(@RequestParam String appointmentStatus, @RequestParam LocalDate date){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentStatusAndSingleDate?appointmentStatus={appointmentStatus}&date={date}";
        List<Object> list = client.get()
                .uri(uri, appointmentStatus, date)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByAppointmentStatusAndDoubleDates")
    public ResponseEntity<List<Object>> fetchAppointmentsByAppointmentStatusAndDoubleDates(@RequestParam String appointmentStatus, @RequestParam LocalDate date1, @RequestParam LocalDate date2){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByAppointmentStatusAndDoubleDates?appointmentStatus={appointmentStatus}&date1={date1}&date2={date2}";
        List<Object> list = client.get()
                .uri(uri, appointmentStatus, date1, date2)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByStatusForToday")
    public ResponseEntity<List<Object>> fetchAppointmentsByStatusForToday(@RequestParam String appointmentStatus){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByStatusForToday?appointmentStatus={appointmentStatus}";
        List<Object> list = client.get()
                .uri(uri, appointmentStatus)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByTypeForToday")
    public ResponseEntity<List<Object>> fetchAppointmentsByTypeForToday(@RequestParam String appointmentType){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByTypeForToday?appointmentType={appointmentType}";
        List<Object> list = client.get()
                .uri(uri, appointmentType)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/appointment/admin-staff/fetchAppointmentsByStatusAndTypeForToday")
    public ResponseEntity<List<Object>> fetchAppointmentsByStatusAndTypeForToday(@RequestParam String appointmentStatus, @RequestParam String appointmentType){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAppointmentsByStatusAndTypeForToday?appointmentStatus={appointmentStatus}&appointmentType={appointmentType}";
        List<Object> list = client.get()
                .uri(uri, appointmentStatus, appointmentType)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/appointment/admin-staff/fetchAllAppointments")
    public ResponseEntity<List<Object>> fetchAllAppointments(){
        WebClient client = webClientBuilder.baseUrl("http://appointment").build();
        String uri = "/appointment/fetchAllAppointments";
        List<Object> list = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
        return ResponseEntity.ok(list);
    }
}
