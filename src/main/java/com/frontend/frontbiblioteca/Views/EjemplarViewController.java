package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Ejemplar;
import com.frontend.frontbiblioteca.WebServiceClient.EjemplarFeingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EjemplarViewController {
    private final EjemplarFeingClient ejemplarFeingClient;

    @PostMapping("/biblioteca/ejemplares")
    @ResponseBody //Le dice a Spring que devuelva datos y no HTML
    public ResponseEntity<?> crearNuevoEjemplar(@RequestBody Ejemplar ejemplar) {
        try {
            ejemplarFeingClient.crearNuevoEjemplar(ejemplar);
            return ResponseEntity.ok().build();
        }
        catch (Exception ex) {
            return ResponseEntity.status(500).body("Error al conectar con el microservicio: "+ ex.getMessage());
        }

    }
}
