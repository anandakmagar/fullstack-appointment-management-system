package com.appointmentschedulingapp.client.service;

import com.appointmentschedulingapp.client.dto.ClientDTO;
import com.appointmentschedulingapp.client.entity.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IClientService {
    long createClient(ClientDTO clientDTO);
    ClientDTO fetchClient(long clientId);
    boolean updateClient(ClientDTO clientDTO);
    boolean deleteByClientId(long clientId);
    Optional<Client> findByClientId(long clientId);
    ClientDTO fetchClientByEmail(String email);
    ClientDTO fetchClientByPhoneNumber(String email);
    String fetchClientNameByClientId(long clientId);
    List<ClientDTO> fetchAllClients();
}
