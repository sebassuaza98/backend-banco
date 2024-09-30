package com.btg.pactual.fpv_fondos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.btg.pactual.fpv_fondos.model.Fondo;
import com.btg.pactual.fpv_fondos.repository.FondoRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class FondoService {
    
    private final FondoRepository fondoRepository;
    public List<Fondo> listarFondos() {
        return fondoRepository.findAll();
    }

    public Fondo crearFondo(Fondo fondo) {
        Fondo nuevoFondo = fondoRepository.save(fondo);     
        return nuevoFondo;
    }
}