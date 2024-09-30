package com.btg.pactual.fpv_fondos.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private String fondoId;
    private String tipo;
    private double monto;
    private LocalDateTime fecha;
    private String clienteId;
}
