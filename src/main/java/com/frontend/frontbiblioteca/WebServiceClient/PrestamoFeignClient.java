package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Prestamo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PrestamoFeignClient", url = "http://localhost:8080/api/prestamos")
public interface PrestamoFeignClient {
    @GetMapping
    List<Prestamo> listarPrestamos();

    @GetMapping("/{id}")
    Prestamo buscarPrestamoPorId(@PathVariable String id);

    @PostMapping
    ResponseEntity<Prestamo> registrarNuevoPrestamo(@RequestBody Prestamo prestamo);

    @PutMapping("/{id}")
    ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable String id ,@RequestBody Prestamo prestamo);

    //Los prestamos no se borran, solo se actualizan
}
