package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Escrito;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "EscirtoFeignClient", url = "http://localhost:8080/api/escrito")
public interface EscritoFeignClient {
    @GetMapping
    List<Escrito> obtenerEscrito();

    @GetMapping("/{id}")
    Escrito obtenerEscritoPorId(@PathVariable String id);

    @PostMapping
    Escrito agregarEscrito(@RequestBody Escrito escrito);
}
