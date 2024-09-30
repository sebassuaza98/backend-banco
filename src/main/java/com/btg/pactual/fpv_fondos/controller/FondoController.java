package com.btg.pactual.fpv_fondos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.btg.pactual.fpv_fondos.model.ApiResponse;
import com.btg.pactual.fpv_fondos.model.Fondo;
import com.btg.pactual.fpv_fondos.services.FondoService;
import com.btg.pactual.fpv_fondos.tools.Constants;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fondos")
@RequiredArgsConstructor
@CrossOrigin(origins = Constants.URL)
public class FondoController {
    
    private final FondoService fondoService;
    private final List<SseEmitter> emitters = new ArrayList<>();

    @GetMapping
    public ResponseEntity<ApiResponse<List<Fondo>>> listarFondos() {
        List<Fondo> fondos = fondoService.listarFondos();
        ApiResponse<List<Fondo>> response = new ApiResponse<>(HttpStatus.OK.value(), Constants.FONDOS, fondos);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Fondo>> crearFondo(@RequestBody Fondo fondo) {
        Fondo nuevoFondo = fondoService.crearFondo(fondo);
        notifyFondoCreado(nuevoFondo);
        ApiResponse<Fondo> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Fondo creado exitosamente", nuevoFondo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void notifyFondoCreado(Fondo fondo) {
        String message = "Se creó un fondo: " + fondo.getNombre();
        emitters.forEach(emitter -> {
            try {
                emitter.send(message);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }

    @GetMapping("/events")
    public SseEmitter streamFondoCreado() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }
}
