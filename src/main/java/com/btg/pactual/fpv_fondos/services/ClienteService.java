package com.btg.pactual.fpv_fondos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.btg.pactual.fpv_fondos.exeption.ClienteNoEncontradoException;
import com.btg.pactual.fpv_fondos.exeption.ClienteYaExisteException;
import com.btg.pactual.fpv_fondos.model.Cliente;
import com.btg.pactual.fpv_fondos.repository.ClienteRepository;
import com.btg.pactual.fpv_fondos.tools.Constants;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.findByIdentificacion(cliente.getIdentificacion()).isPresent()) {
            throw new ClienteYaExisteException(Constants.NOT_CLIENTE + cliente.getIdentificacion());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarSaldo(String identificacion, double nuevoSaldo) {
        if (nuevoSaldo < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteNoEncontradoException(identificacion));
        cliente.setSaldo(nuevoSaldo);
        return clienteRepository.save(cliente);
    }

    public Cliente filtrarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteNoEncontradoException(identificacion));
    }
}
