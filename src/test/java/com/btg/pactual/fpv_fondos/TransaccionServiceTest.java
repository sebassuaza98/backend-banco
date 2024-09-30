package com.btg.pactual.fpv_fondos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.btg.pactual.fpv_fondos.services.ClienteService;
import com.btg.pactual.fpv_fondos.services.TransaccionService;
import com.btg.pactual.fpv_fondos.tools.Constants;

import java.util.Optional;
import java.time.LocalDateTime;

class TransaccionServiceTest {

    @Mock
    private FondoRepository fondoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private HistoryTransaccionRepository historyTransaccionRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private TransaccionService transaccionService;

    private Fondo testFondo;
    private Cliente testCliente;
    private Transaccion testTransaccion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testFondo = new Fondo();
        testFondo.setMontoMinimo(1000.0);

        testCliente = new Cliente();
        testCliente.setSaldo(5000.0);

        testTransaccion = new Transaccion();
        testTransaccion.setFondoId("fondo123");
        testTransaccion.setMonto(2000.0);
        testTransaccion.setTipo(Constants.TIPOA);
        testTransaccion.setClienteId("cliente123");
        testTransaccion.setFecha(LocalDateTime.now());
    }

    @Test
    void suscribirseAFondo_success() {
        when(fondoRepository.findById("fondo123")).thenReturn(Optional.of(testFondo));
        when(clienteRepository.findByIdentificacion("cliente123")).thenReturn(Optional.of(testCliente));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(testTransaccion);

        Transaccion result = transaccionService.suscribirseAFondo("fondo123", "cliente123", 2000.0);

        assertNotNull(result);
        assertEquals("fondo123", result.getFondoId());
        assertEquals(2000.0, result.getMonto());
        assertEquals(Constants.TIPOA, result.getTipo());

        verify(clienteService).actualizarSaldo("cliente123", 3000.0); // 5000 - 2000 = 3000
        verify(historyTransaccionRepository).save(any(HistoryTransaccion.class));
    }

    @Test
    void suscribirseAFondo_fondoNoEncontrado() {
        when(fondoRepository.findById("fondo123")).thenReturn(Optional.empty());

        assertThrows(FondoNoEncontradoException.class, () -> {
            transaccionService.suscribirseAFondo("fondo123", "cliente123", 2000.0);
        });
    }

    @Test
    void suscribirseAFondo_clienteNoEncontrado() {
        when(fondoRepository.findById("fondo123")).thenReturn(Optional.of(testFondo));
        when(clienteRepository.findByIdentificacion("cliente123")).thenReturn(Optional.empty());

        assertThrows(ClienteNoEncontradoException.class, () -> {
            transaccionService.suscribirseAFondo("fondo123", "cliente123", 2000.0);
        });
    }

    @Test
    void suscribirseAFondo_montoInsuficiente() {
        when(fondoRepository.findById("fondo123")).thenReturn(Optional.of(testFondo));
        when(clienteRepository.findByIdentificacion("cliente123")).thenReturn(Optional.of(testCliente));

        assertThrows(RuntimeException.class, () -> {
            transaccionService.suscribirseAFondo("fondo123", "cliente123", 500.0); // Monto menor al mínimo
        });
    }

    @Test
    void suscribirseAFondo_saldoInsuficiente() {
        testCliente.setSaldo(1000.0);

        when(fondoRepository.findById("fondo123")).thenReturn(Optional.of(testFondo));
        when(clienteRepository.findByIdentificacion("cliente123")).thenReturn(Optional.of(testCliente));

        assertThrows(SaldoInsuficienteException.class, () -> {
            transaccionService.suscribirseAFondo("fondo123", "cliente123", 2000.0);
        });
    }
        @Test
    void cancelarFondo_success() {
        Transaccion transaccionApertura = new Transaccion();
        transaccionApertura.setTipo(Constants.TIPOA);
        transaccionApertura.setMonto(2000.0);
        transaccionApertura.setClienteId("cliente123");

        when(transaccionRepository.findById("transaccion123")).thenReturn(Optional.of(transaccionApertura));
        when(clienteRepository.findByIdentificacion("cliente123")).thenReturn(Optional.of(testCliente));

        Transaccion result = transaccionService.cancelarFondo("cliente123", "transaccion123");
        assertEquals(Constants.TIPOB, result.getTipo());
        verify(transaccionRepository).save(transaccionApertura);

        verify(clienteService).actualizarSaldo("cliente123", 7000.0); // 5000 + 2000
        verify(historyTransaccionRepository).save(any(HistoryTransaccion.class));
    }

    @Test
    void cancelarFondo_transaccionNoEncontrada() {
        when(transaccionRepository.findById("transaccion123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            transaccionService.cancelarFondo("cliente123", "transaccion123");
        });
    }

    @Test
    void cancelarFondo_tipoIncorrecto() {
        Transaccion transaccionApertura = new Transaccion();
        transaccionApertura.setTipo(Constants.TIPOB);

        when(transaccionRepository.findById("transaccion123")).thenReturn(Optional.of(transaccionApertura));

        assertThrows(RuntimeException.class, () -> {
            transaccionService.cancelarFondo("cliente123", "transaccion123");
        });
    }

    @Test
    void cancelarFondo_clienteNoCoincide() {
        Transaccion transaccionApertura = new Transaccion();
        transaccionApertura.setTipo(Constants.TIPOA);
        transaccionApertura.setClienteId("otroCliente123");

        when(transaccionRepository.findById("transaccion123")).thenReturn(Optional.of(transaccionApertura));

        assertThrows(RuntimeException.class, () -> {
            transaccionService.cancelarFondo("cliente123", "transaccion123");
        });
    }

}
