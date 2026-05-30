package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Miembro {
    String idMiembro;
    String nombre;
    String apellido;
    String telefono;
    String correo;
    String direccion;
    boolean estado;
}
