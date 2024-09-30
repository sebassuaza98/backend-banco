package com.btg.pactual.fpv_fondos.exeption;


public class ClienteYaExisteException extends RuntimeException {
    public ClienteYaExisteException(String mensaje) {
        super(mensaje);
    }
}
