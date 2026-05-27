package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Editorial {
    String id;
    String nombre;
    String direccion;
    String telefono;
}
