package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Editorial;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "EditorialFeignClient", url = "http://localhost:8080/api/editoriales")
public interface EditorialFeignClient {
    @GetMapping
    List<Editorial> obtenerEditoriales();

    @PutMapping("/{id}")
    Editorial  actualizarEditorial(@PathVariable String id, @RequestBody Editorial editorial);
}
