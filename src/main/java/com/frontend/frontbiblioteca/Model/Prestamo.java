package com.frontend.frontbiblioteca.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    String idPrestamo;
    LocalDate fechaPrestamo;
    LocalDate fechaDevolucion;
    Miembro miembro;
    Ejemplar ejemplar;
    Bibliotecario bibliotecario;
}
