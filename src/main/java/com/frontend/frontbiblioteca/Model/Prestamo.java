package com.frontend.frontbiblioteca.Model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Prestamo {
    String idPrestamo;
    LocalDate fechaPrestamo;
    LocalTime fechaDevolucion;
    Miembro miembro;
    Ejemplar ejemplar;
    Bibliotecario bibliotecario;
}
