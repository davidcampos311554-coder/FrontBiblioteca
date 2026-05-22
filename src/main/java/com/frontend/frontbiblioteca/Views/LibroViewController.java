package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Libro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return "libro/listar-libros"; //El String que retorna busca en templates /libro/listar-libros.html
    }

    //Buscar libro especifico
    @GetMapping("/biblioteca/detalleLibro/{isbn}")
    public String buscarLibro(Model model,@PathVariable String isbn) {
        try{
             ResponseEntity<Libro> response = libroRestClient.buscarLibroPorIsbn(isbn);

             if (response.getStatusCode().is2xxSuccessful()) {
                 model.addAttribute("detalleLibro", response.getBody());
                 return "libro/detalle-libro";
             }

             if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                 model.addAttribute("errorMensaje", "El servidor de DB esta fuera de servicio. Intente más tarde.");
             }
             else {
                 model.addAttribute("errorMensaje", "No se encuentra el libro");
             }
        }
        catch (Exception e){
            model.addAttribute("errorMensaje", "El servidor no está disponible en este momento");
        }

        return "libro/detalle-libro-error";
    }

    //Antes de registrar, creamos un modelo vacío que el usuario llena
    @GetMapping("/biblioteca/registrarLibro")
    public String registrarLibro(Model model) {
        model.addAttribute("libroNuevo", new Libro());
        return "libro/registrar-libro";
    }

    @PostMapping("/biblioteca/registrarLibro")
    public String guardarLibro(@ModelAttribute("libroNuevo") Libro libroNuevo, Model model) {
        try {
            ResponseEntity<Libro> response = libroRestClient.crearNuevoLibro(libroNuevo);

            // 1. Registro exitoso (Código 200 / 201)
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/listar-libros";
            }

            // Responde código de error (404 o 500)
            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio. Intenta más tarde.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo registrar el libro. Verifica los datos.");
            }

        } catch (Exception e) {
            //Fallo total de comunicación
            model.addAttribute("errorMensaje", "El servicio no está disponible en este momento. Intenta más tarde.");
        }

        // Si falló por cualquier razón, volvemos a mostrar el formulario con el mensaje correspondiente
        return "libro/registrar-libro";
    }

    //Los formularios HTML5 nativos no soportanan PUT ni DELETE, así que lo haremos con POST
    @GetMapping("/biblioteca/actualizarLibro/{isbn}")
    public String actualizarLibro(Model model, @PathVariable String isbn) {
        //Reutilizamos la lógica de buscarLibro
        String vistaResultado = buscarLibro(model, isbn);
        if ("libro/detalle-libro".equals(vistaResultado)) {
            //Almacenamos en varible el libro de 'buscarLibro'
            Libro libroEncontrado = (Libro) model.getAttribute("detalleLibro");
            model.addAttribute("actualizarLibro", libroEncontrado);

            return "libro/actualizar-libro";
        }
        return vistaResultado;
    }

    @PostMapping("/biblioteca/actualizarLibro")
    public String libroActualizado(@ModelAttribute("actualizarLibro") Libro libroActualizar, Model model) {
        try{
            ResponseEntity<Libro> response = libroRestClient.actualizarLibro(libroActualizar.getIsbn(), libroActualizar);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/detalleLibro/"+libroActualizar.getIsbn();
            }
            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de BD está fuera de servicio. Intenta más tarde.");
            }
            else {
                model.addAttribute("errorMensaje", "No se pudo actualizar el libro. Verifica los datos.");
            }
        }
        catch (Exception e){
            model.addAttribute("errorMensaje", "El servicio no está disponible en este momento. Intenta más tarde.");
        }
        return "libro/actualizar-libro";
    }

    //La eliminación se puede hacer con un GET
    @GetMapping("/biblioteca/eliminarLibro/{isbn}")
    public String eliminarLibro(@PathVariable String isbn, Model model) {
        try{
            ResponseEntity<Libro> response = libroRestClient.eliminarLibro(isbn);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/listar-libros";
            }

            if  (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de BD está fuera de servicio. Intenta más tarde.");
            }
            else {
                model.addAttribute("errorMensaje", "No se pudo eliminar el libro. Verifica los datos.");
            }
        }
        catch (Exception e){
            model.addAttribute("errorMensaje", "El servicio no está disponible en este momento. Intenta más tarde.");
        }
        //Traemos la lista para que el métod tenga algo que mostrar al fallar
        model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());
        return "libro/listar-libros";
    }
}
