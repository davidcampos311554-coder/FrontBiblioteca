package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Autor {
    String idAutor;
    String nombre;
    String apellido;
    String nacionalidad;
}
