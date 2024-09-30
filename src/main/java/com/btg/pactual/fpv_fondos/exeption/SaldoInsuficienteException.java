package com.btg.pactual.fpv_fondos.exeption;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente en la cuenta del cliente");
    }
}