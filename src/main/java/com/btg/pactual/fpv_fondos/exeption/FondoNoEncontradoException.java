package com.btg.pactual.fpv_fondos.exeption;

public class FondoNoEncontradoException extends RuntimeException {
    public FondoNoEncontradoException(String fondoId) {
        super("Fondo no encontrado: " + fondoId);
    }
}