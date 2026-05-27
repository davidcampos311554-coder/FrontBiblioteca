package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Libro {
    String isbn;
    String titulo;
    Integer numPag;
    Editorial editorial;
}
