package com.frontend.frontbiblioteca.Util;

import com.frontend.frontbiblioteca.Model.Escrito;
import com.frontend.frontbiblioteca.Model.Libro;
import com.frontend.frontbiblioteca.Model.Autor;

import java.time.LocalDate;
import java.util.List;

public class EscritoUtil {

    // Al añadir 'static', se puede usar sin necesidad de hacer "new EscritoUtil()"
    public static Escrito generarEscritoConIdNuevo(List<Escrito> todosLosEscritos, Libro libro, String idAutor) {
        int maxNumero = 0;
        for (Escrito esc : todosLosEscritos) {
            if (esc.getId() != null && esc.getId().startsWith("E")) {
                try {
                    int numeroActual = Integer.parseInt(esc.getId().substring(1));
                    if (numeroActual > maxNumero) {
                        maxNumero = numeroActual;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        String nuevoIdEscrito = String.format("E%03d", maxNumero + 1);

        Autor autorCascaron = new Autor();
        autorCascaron.setIdAutor(idAutor);

        Escrito escritoNuevo = new Escrito();
        escritoNuevo.setId(nuevoIdEscrito);
        escritoNuevo.setFechaEscrito(LocalDate.now());
        escritoNuevo.setCiudad("Sin especificar");
        escritoNuevo.setLibro(libro);
        escritoNuevo.setAutor(autorCascaron);

        return escritoNuevo;
    }
}