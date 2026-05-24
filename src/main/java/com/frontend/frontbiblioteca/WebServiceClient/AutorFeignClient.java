package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Autor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="autorFeignClient", url="http://localhost:8080/api/autores")
public interface AutorFeignClient {
   //Traer todos los autores
    @GetMapping
    List<Autor> obtenerAutores();

    @GetMapping("/{id}")
    Autor obtenerAutorPorId(@PathVariable String id);
}