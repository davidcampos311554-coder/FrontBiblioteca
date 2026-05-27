package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    String isbn;
    String titulo;
    Integer numPag;
    Editorial editorial;
    Boolean estado;
}
