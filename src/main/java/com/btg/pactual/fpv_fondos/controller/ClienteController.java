package com.btg.pactual.fpv_fondos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.btg.pactual.fpv_fondos.model.ApiResponse;
import com.btg.pactual.fpv_fondos.model.Cliente;
import com.btg.pactual.fpv_fondos.services.ClienteService;
import com.btg.pactual.fpv_fondos.tools.Constants;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = Constants.URL)
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente createdClient = clienteService.crearCliente(cliente);
        
        ApiResponse<Cliente> response = new ApiResponse<>(
            HttpStatus.CREATED.value(), Constants.CLIENTE_CREATED, 
            createdClient
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/filtrar/{identificacion}")
    public ResponseEntity<ApiResponse<Cliente>> filtrarPorIdentificacion(@PathVariable String identificacion) {
        Cliente cliente = clienteService.filtrarPorIdentificacion(identificacion);
        
        ApiResponse<Cliente> response = new ApiResponse<>(
            HttpStatus.OK.value(), Constants.CLIENTE_NOT, 
            cliente
        );
        
        return ResponseEntity.ok(response);
    }
}
