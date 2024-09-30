package com.btg.pactual.fpv_fondos.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.btg.pactual.fpv_fondos.model.Transaccion;

import java.util.List;

public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteId(String clienteId);
    List<Transaccion> findByClienteIdAndTipo(String clienteId, String tipo);
}

