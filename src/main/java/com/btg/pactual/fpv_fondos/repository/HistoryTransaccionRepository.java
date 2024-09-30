package com.btg.pactual.fpv_fondos.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.btg.pactual.fpv_fondos.model.HistoryTransaccion;

import java.util.List;

public interface HistoryTransaccionRepository extends MongoRepository<HistoryTransaccion, String> {
    List<HistoryTransaccion> findByClienteId(String clienteId);
}
