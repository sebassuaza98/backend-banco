package com.btg.pactual.fpv_fondos.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.btg.pactual.fpv_fondos.model.Fondo;

public interface FondoRepository extends MongoRepository<Fondo, String> {
}
