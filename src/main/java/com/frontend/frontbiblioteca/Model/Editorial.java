package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Editorial {
    String id;
    String nombre;
    String direccion;
    String telefono;
}
