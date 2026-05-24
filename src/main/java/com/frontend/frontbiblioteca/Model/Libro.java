package com.frontend.frontbiblioteca.Model;

import lombok.Data;

@Data
public class Libro {
    String isbn;
    String titulo;
    Integer numPag;
    Editorial editorial;
}
