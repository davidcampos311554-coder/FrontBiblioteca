package com.frontend.frontbiblioteca.Model;

import lombok.Data;

import java.util.Date;

@Data
public class Escrito {
    String id;
    Date fechascrito;
    String ciudad;
    Libro libro;
    Autor autor;
}
