package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Escrito {
    String id;
    LocalDate fechaEscrito;
    String ciudad;
    Libro libro;
    Autor autor;
}
