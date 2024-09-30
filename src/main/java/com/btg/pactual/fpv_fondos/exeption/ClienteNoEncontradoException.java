package com.btg.pactual.fpv_fondos.exeption;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String identificacion) {
        super("Cliente no encontrado: " + identificacion);
    }
}

