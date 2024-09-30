package com.btg.pactual.fpv_fondos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.btg.pactual.fpv_fondos.exeption.ClienteNoEncontradoException;
import com.btg.pactual.fpv_fondos.exeption.FondoNoEncontradoException;
import com.btg.pactual.fpv_fondos.exeption.SaldoInsuficienteException;
import com.btg.pactual.fpv_fondos.model.Cliente;
import com.btg.pactual.fpv_fondos.model.Fondo;
import com.btg.pactual.fpv_fondos.model.HistoryTransaccion;
import com.btg.pactual.fpv_fondos.model.Transaccion;
import com.btg.pactual.fpv_fondos.repository.ClienteRepository;
import com.btg.pactual.fpv_fondos.repository.FondoRepository;
import com.btg.pactual.fpv_fondos.repository.HistoryTransaccionRepository;
import com.btg.pactual.fpv_fondos.repository.TransaccionRepository;
import com.btg.pactual.fpv_fondos.tools.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    
    private final FondoRepository fondoRepository;
    private final TransaccionRepository transaccionRepository;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final HistoryTransaccionRepository historyTransaccionRepository;
    
    public Transaccion suscribirseAFondo(String fondoId, String identificacion, double monto) {
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new FondoNoEncontradoException(fondoId));

        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteNoEncontradoException(identificacion));

        if (monto < fondo.getMontoMinimo()) {
            throw new RuntimeException(Constants.MONTO);
        }

        double nuevoSaldo = cliente.getSaldo() - monto;
        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException();
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setFondoId(fondoId);
        transaccion.setMonto(monto);
        transaccion.setTipo(Constants.TIPOA);
        transaccion.setClienteId(identificacion);
        transaccion.setFecha(LocalDateTime.now());

        clienteService.actualizarSaldo(identificacion, nuevoSaldo);

        try {
            Transaccion savedTransaccion = transaccionRepository.save(transaccion);
            
            HistoryTransaccion historyTransaccion = new HistoryTransaccion(
                fondoId, Constants.TIPOA, monto, LocalDateTime.now(), identificacion);
            historyTransaccionRepository.save(historyTransaccion);

            return savedTransaccion;
        } catch (Exception e) {
            throw new RuntimeException(Constants.Error + e.getMessage(), e);
        }
    }

    

    public Transaccion cancelarFondo(String identificacion, String transaccionId) {
        Transaccion transaccionApertura = transaccionRepository.findById(transaccionId)
            .orElseThrow(() -> new RuntimeException(Constants.ERRORTRC));
    
        if (Constants.TIPOB.equals(transaccionApertura.getTipo())) {
            throw new RuntimeException("La transacción ya ha sido cancelada");
        }
        if (!transaccionApertura.getClienteId().equals(identificacion)) {
            throw new RuntimeException(Constants.IDTRC);
        }
    
        transaccionApertura.setTipo(Constants.TIPOB);
        transaccionApertura.setFecha(LocalDateTime.now()); 
        transaccionRepository.save(transaccionApertura);
    
        HistoryTransaccion historyTransaccionCancelacion = new HistoryTransaccion(
            transaccionApertura.getFondoId(), Constants.TIPOB, transaccionApertura.getMonto(),
            LocalDateTime.now(), identificacion);
        historyTransaccionRepository.save(historyTransaccionCancelacion);

        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteNoEncontradoException(identificacion));
    
        double montoADevolver = transaccionApertura.getMonto();
        double nuevoSaldo = cliente.getSaldo() + montoADevolver;
        clienteService.actualizarSaldo(identificacion, nuevoSaldo);
    
        return transaccionApertura;
    }

    public List<HistoryTransaccion> historyTransaccionsByClienteId(String clienteId) {
        List<HistoryTransaccion> historyTransacciones = historyTransaccionRepository.findByClienteId(clienteId);
        return historyTransacciones;
    }
    
    public List<Transaccion> obtenerTransaccionesPorCliente(String clienteId, String tipo) {
        if (tipo != null) {
            List<Transaccion> transacciones = transaccionRepository.findByClienteIdAndTipo(clienteId, tipo);
            return transacciones;
        }
        return transaccionRepository.findByClienteId(clienteId);
    }


    
    
}
