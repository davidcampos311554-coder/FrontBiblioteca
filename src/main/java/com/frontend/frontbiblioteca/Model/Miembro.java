package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Miembro {
    String idMiembro;
    String nombre;
    String apellido;
    String telefono;
    String correo;
    String direccion;
}
