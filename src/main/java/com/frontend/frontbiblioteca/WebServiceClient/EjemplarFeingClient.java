package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Ejemplar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "EjemplarFeignClient", url = "http://localhost:8080/api/ejemplares")
public interface EjemplarFeingClient {
    //Traer todos los ejemplares
    @GetMapping
    List<Ejemplar> obtenerEjemplares();

    //Actualizar
    @PutMapping("/{id}")
    Ejemplar actualizarEjemplar(@PathVariable String id, @RequestBody Ejemplar ejemplar);

    //Crear
    @PostMapping
    Ejemplar crearNuevoEjemplar(@RequestBody Ejemplar ejemplar);

    //Eliinar si se pierde o se destruye
    @DeleteMapping("/{id}")
    void eliminarEjemplar(@PathVariable String id);
}
