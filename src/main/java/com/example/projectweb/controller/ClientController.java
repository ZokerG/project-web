package com.example.projectweb.controller;

import com.example.projectweb.model.Client;
import com.example.projectweb.request.ClientRequest;
import com.example.projectweb.response.LoginResponse;
import com.example.projectweb.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> findAllClient(){
        return clientService.findAllClient();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable long id){
        return clientService.findById(id);
    }

    @PostMapping
    public Client create(@RequestBody ClientRequest request) {
        return clientService.create(request);
    }

    @GetMapping("/email")
    public Client findByEmail(String email) {
        return clientService.findByEmail(email);
    }

    @PostMapping("/signin")
    public LoginResponse login(@RequestParam String email, @RequestParam String password) {
        return clientService.login(email, password);
    }
}
