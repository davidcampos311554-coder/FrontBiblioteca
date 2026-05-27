package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Bibliotecario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "BibliotecarioFeignClient", url = "http://localhost:8080/api/bibliotecarios")
public interface BibliotecarioFeignClient {
    @GetMapping
    List<Bibliotecario> obtenerBibliotecarios();
}
