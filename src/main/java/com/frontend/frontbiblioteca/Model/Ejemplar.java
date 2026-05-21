package com.frontend.frontbiblioteca.Model;

import lombok.Data;

@Data
public class Ejemplar {
    String id;
    String ubicacion;
    boolean estado;
    Libro libro;
}
