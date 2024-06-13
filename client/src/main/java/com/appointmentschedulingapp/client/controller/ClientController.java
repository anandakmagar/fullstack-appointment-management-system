package com.appointmentschedulingapp.client.controller;
import com.appointmentschedulingapp.client.dto.ClientDTO;
import com.appointmentschedulingapp.client.dto.CreateClientResponseDTO;
import com.appointmentschedulingapp.client.dto.ResponseDTO;
import com.appointmentschedulingapp.client.service.IClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private IClientService iClientService;

    @PostMapping("/create")
    public ResponseEntity<CreateClientResponseDTO> createClient(@Valid @RequestBody ClientDTO clientDTO){
        long clientId = iClientService.createClient(clientDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateClientResponseDTO("201", "Client created successfully and your client Id is given below", clientId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteByClientId(@RequestParam long clientId){
        boolean isDeleted = iClientService.deleteByClientId(clientId);
        if (isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("200", "Client deleted successfully"));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "An error occurred! Please try again later."));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> update(@RequestBody ClientDTO clientDTO){
        boolean isUpdated = iClientService.updateClient(clientDTO);
        if (isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("200", "Client updated successfully"));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "An error occurred! Please try again later."));
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<ClientDTO> fetchClient(@RequestParam long clientId){
        ClientDTO clientDTO = iClientService.fetchClient(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
    }

    @GetMapping("/fetchClientByClientId")
    public ClientDTO fetchClientByClientId(@RequestParam long clientId){
        return iClientService.fetchClient(clientId);
    }

    @GetMapping("/fetchClientByEmail")
    public ClientDTO fetchClientByEmail(@RequestParam String email){
        return iClientService.fetchClientByEmail(email);
    }

    @GetMapping("/fetchClientByPhoneNumber")
    public ClientDTO fetchClientByPhoneNumber(@RequestParam String phoneNumber){
        return iClientService.fetchClientByPhoneNumber(phoneNumber);
    }

    @GetMapping("/fetchClientNameByClientId")
    public String fetchClientNameByClientId(@RequestParam long clientId){
        return iClientService.fetchClientNameByClientId(clientId);
    }

    @GetMapping("/fetchAllClients")
    public List<ClientDTO> fetchAllClients(){
        return iClientService.fetchAllClients();
    }
}
