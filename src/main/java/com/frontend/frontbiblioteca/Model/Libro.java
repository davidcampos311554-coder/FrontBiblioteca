package com.frontend.frontbiblioteca.Model;

import lombok.Data;

@Data
public class Libro {
    String isbn;
    String titulo;
    int numPag;
    Editorial editorial;
}
