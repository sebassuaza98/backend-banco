package com.btg.pactual.fpv_fondos.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.btg.pactual.fpv_fondos.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Optional<Cliente> findByIdentificacion(String identificacion);
}
