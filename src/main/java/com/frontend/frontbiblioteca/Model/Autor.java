package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Autor {
    String idAutor;
    String nombre;
    String apellido;
    String nacionalidad;
}
