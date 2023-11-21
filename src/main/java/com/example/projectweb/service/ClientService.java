package com.example.projectweb.service;

import com.example.projectweb.jwt.JwtService;
import com.example.projectweb.model.Client;
import com.example.projectweb.repository.IClient;
import com.example.projectweb.request.ClientRequest;
import com.example.projectweb.response.LoginResponse;
import com.example.projectweb.role.ClientesEnum;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final IClient clientRepository;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;

    public ClientService(IClient clientRepository, JwtService jwtService, AuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.jwtService = jwtService;
        this.authenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    public boolean existsById(long id) {
        return clientRepository.findById(id).isPresent();
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow();
    }

    public List<Client> findAllClient(){
        return clientRepository.findAll();
    }

    public Client findById(long id){
        return clientRepository.findById(id).orElseThrow();
    }

    public Client create(ClientRequest request){

        if (existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new RuntimeException("Password and password confirm are not equals");
        }

        if (request.getCharge().equals("ADMIN")){
            Client client = Client.builder().name(request.getName()).lastName(request.getLastName())
                    .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).address(request.getAddress())
                    .phone(request.getPhone()).userName(request.getUserName()).role(ClientesEnum.ADMIN).build();

            return clientRepository.save(client);
        }

        Client client = Client.builder().name(request.getName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).address(request.getAddress())
                .phone(request.getPhone()).userName(request.getUserName()).role(ClientesEnum.USER).build();

        return clientRepository.save(client);
    }

    public LoginResponse login(String userName, String password) {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        Client client = clientRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Client not found"));
        String token = jwtService.getToken(client);
        return LoginResponse.builder().token(token).role(client.getRole().name()).name(client.getName()).id(client.getId()).build();
    }
}
