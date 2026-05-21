package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Libro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.frontend.frontbiblioteca.WebServiceClient.LibroRestClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor //Evitar poner el constructor de libroRestClient
public class LibroViewController {
    private final LibroRestClient libroRestClient;

    @GetMapping("/biblioteca/listar-libros")
    public String getLibros(Model model) { //Los ViewController SIEMPRE retornan un String
        model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());
        return "libro/listar-libros"; //El String que retorna
    }

    //Buscar libro especifico
    @GetMapping("biblioteca/buscarLibro/{isbn}")
    public String buscarLibro(Model model,@PathVariable String isbn) {
        Libro libroBuscado = libroRestClient.buscarLibroPorIsbn(isbn).getBody();
        model.addAttribute("detalleLibro", libroBuscado);
        return "libro/detalle-libro";
    }

    //Antes de registrar, creamos un modelo vacío que el usuario llena
    @GetMapping("/biblioteca/registrarLibro")
    public String registrarLibro(Model model) {
        model.addAttribute("libroNuevo", new Libro());
        return "libro/registrar-libro";
    }

    @PostMapping("/biblioteca/registrarLibro")
    public String guardarLibro(@ModelAttribute("libroNuevo") Libro libroNuevo) {
        libroRestClient.crearNuevoLibro(libroNuevo);
        return "redirect:/biblioteca/listar-libros";
    }

    //Los formularios HTML5 nativos no soportanan PUT ni DELETE, así que lo haremos con POST
    @GetMapping("/biblioteca/actualizarLibro/{isbn}")
    public String actualizarLibro(Model model, @PathVariable String isbn) {
        Libro libroExistente = libroRestClient.buscarLibroPorIsbn(isbn).getBody();
        model.addAttribute("actualizarLibro", libroExistente);
        return "libro/actualizar-libro";
    }

    @PostMapping("/biblioteca/actualizarLibro/")
    public String libroActualizado(@ModelAttribute("actualizarLibro") Libro libroActualizar) {
        libroRestClient.actualizarLibro(libroActualizar.getIsbn(), libroActualizar);
        return "redirect:/biblioteca/detalle-libro";
    }

    //La eliminación se puede hacer con un GET
    @GetMapping("/biblioteca/eliminarLibro/{isbn}")
    public String eliminarLibro(@PathVariable String isbn) {
        libroRestClient.eliminarLibro(isbn);
        return "redirect:/biblioteca/listar-libros";
    }
}
