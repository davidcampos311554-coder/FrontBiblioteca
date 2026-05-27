package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ejemplar {
    String id;
    String ubicacion;
    boolean estado;
    Libro libro;
}
