package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Escrito {
    String id;
    LocalDate fechaEscrito;
    String ciudad;
    Libro libro;
    Autor autor;
}
