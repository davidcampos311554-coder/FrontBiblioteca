package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Miembro;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(name = "MimebroFeingClient", url = "http://localhost:8080/api/miembro")
public interface MiembroFeingClient {
    @GetMapping
    List<Miembro> listarTodosMiembros();

    @PostMapping
    ResponseEntity<Miembro> crearMiembro(@RequestBody Miembro miembro);

    @PutMapping("/{id}")
    ResponseEntity<Miembro> actualizarMiembro(@PathVariable String id, @RequestBody Miembro miembro);

    @DeleteMapping("/{id}")
    ResponseEntity<Miembro> eliminarMiembro(@PathVariable("id") String id);
}
