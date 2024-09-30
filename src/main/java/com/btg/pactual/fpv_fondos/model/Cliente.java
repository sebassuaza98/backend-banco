package com.btg.pactual.fpv_fondos.model;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String id;

    @Field("identificacion")
    @NotBlank(message = "La identificación es obligatoria")
    @Size(min = 6, max = 20, message = "La identificación debe tener entre 6 y 20 caracteres")
    private String identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String correo;

    @NotNull(message = "El saldo es obligatorio")
    @Min(value = 0, message = "El saldo no puede ser negativo")
    private Double saldo;
}
