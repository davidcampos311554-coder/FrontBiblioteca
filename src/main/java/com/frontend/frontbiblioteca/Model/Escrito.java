package com.frontend.frontbiblioteca.Model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Escrito {
    String id;
    LocalDate fechaEscrito;
    String ciudad;
    Libro libro;
    Autor autor;
}
