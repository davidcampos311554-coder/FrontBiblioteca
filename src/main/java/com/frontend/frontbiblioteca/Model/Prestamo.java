package com.frontend.frontbiblioteca.Model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Prestamo {
    String idPrestamo;
    LocalDate fechaPrestamo;
    LocalTime fechaDevolucion;
    Miembro miembro;
    Ejemplar ejemplar;
    Bibliotecario bibliotecario;
}
