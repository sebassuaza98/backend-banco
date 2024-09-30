package com.btg.pactual.fpv_fondos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.btg.pactual.fpv_fondos.model.ApiResponse;
import com.btg.pactual.fpv_fondos.model.HistoryTransaccion;
import com.btg.pactual.fpv_fondos.model.Transaccion;
import com.btg.pactual.fpv_fondos.services.TransaccionService;
import com.btg.pactual.fpv_fondos.tools.Constants;

import java.util.List;

@RestController
@RequestMapping("/api/transaccion")
@RequiredArgsConstructor
@CrossOrigin(origins = Constants.URL) 
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping("/suscribirse")
    public ResponseEntity<ApiResponse<Transaccion>> suscribirseAFondo(@RequestParam String fondoId, @RequestParam String identificacion, @RequestParam double monto) {
        Transaccion transaccionCreada = transaccionService.suscribirseAFondo(fondoId, identificacion, monto);
        
        ApiResponse<Transaccion> response = new ApiResponse<>(
            HttpStatus.CREATED.value(), Constants.SUBSCRIPCION,
            transaccionCreada
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/cancelar")
    public ResponseEntity<ApiResponse<Transaccion>> cancelarFondo(@RequestParam String identificacion, @RequestParam String transaccionId) {
        Transaccion transaccionCancelada = transaccionService.cancelarFondo(identificacion, transaccionId);
        
        ApiResponse<Transaccion> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            Constants.FONDO_CANCELADO,
            transaccionCancelada
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cliente/{clienteId}/transacciones")
    public ResponseEntity<ApiResponse<List<Transaccion>>> obtenerTransaccionesPorCliente(
            @PathVariable String clienteId, 
            @RequestParam(required = false) String tipo) {

        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCliente(clienteId, tipo);
        
        ApiResponse<List<Transaccion>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            Constants.CANCELADAS,
            transacciones
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}/history-transacciones")
    public ResponseEntity<ApiResponse<List<HistoryTransaccion>>> obtenerHistorialTransaccionesPorCliente(
            @PathVariable String clienteId) {

        List<HistoryTransaccion> historyTransacciones = transaccionService.historyTransaccionsByClienteId(clienteId);

        ApiResponse<List<HistoryTransaccion>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            Constants.HISTORIAL,
            historyTransacciones
        );

        return ResponseEntity.ok(response);
    }
}
