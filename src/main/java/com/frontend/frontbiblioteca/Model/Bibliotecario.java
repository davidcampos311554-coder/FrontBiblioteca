package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bibliotecario {
    String idBibliotecario;
    String nombre;
    String apellido;
}
