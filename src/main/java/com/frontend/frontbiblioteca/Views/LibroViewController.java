package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Autor;
import com.frontend.frontbiblioteca.Model.Ejemplar;
import com.frontend.frontbiblioteca.Model.Escrito;
import com.frontend.frontbiblioteca.Model.Libro;
import com.frontend.frontbiblioteca.WebServiceClient.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.*;

@Controller
@RequiredArgsConstructor
public class LibroViewController {

    private final LibroRestClient libroRestClient;
    private final EditorialFeignClient editorialFeignClient;
    private final AutorFeignClient autorFeignClient;
    private final EjemplarFeingClient ejemplarFeingClient;
    private final EscritoFeignClient escritoFeignClient;

    //PANTALLA UNICA. Lista los libros y prepara los ejemplares
    @GetMapping("/biblioteca/listar-libros")
    public String getLibros(Model model) {
        try {
            model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());

            //Informacion
            model.addAttribute("listaEditoriales", editorialFeignClient.obtenerEditoriales());
            model.addAttribute("listaAutores", autorFeignClient.obtenerAutores());
            model.addAttribute("listaEjemplares", ejemplarFeingClient.obtenerEjemplares());
            model.addAttribute("listaEscrito", escritoFeignClient.obtenerEscrito());
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "Hubo un problema al conectar con los servicios backend.");
        }

        if (!model.containsAttribute("libroNuevo")) {
            model.addAttribute("libroNuevo", Libro.builder().build());
        }
        return "libro/listar-libros";
    }

    @GetMapping("/biblioteca/api/libro-detalle/{isbn}")
    @ResponseBody // Esto es clave para que devuelva JSON directamente a JavaScript
    public Map<String, Object> obtenerDetalleLibroJson(@PathVariable String isbn) {
        try {
            // Usamos el estándar de Feign para traer los ejemplares
            List<Ejemplar> todosEjemplares = ejemplarFeingClient.obtenerEjemplares();

            // Filtramos en el servidor los que corresponden al libro seleccionado
            List<Ejemplar> ejemplaresFiltrados = todosEjemplares.stream()
                    .filter(e -> e.getLibro() != null && isbn.equals(e.getLibro().getIsbn()))
                    .toList();

            //Traemos los escritos para unir con el autor
            List<Escrito> todosEscritos = escritoFeignClient.obtenerEscrito();

            String nombresAutores = todosEscritos.stream()
                    .filter(e -> e.getLibro() != null && isbn.equals(e.getLibro().getIsbn()))
                    .map(e ->
                            {String idAutor = e.getAutor().getIdAutor();
                            return autorFeignClient.obtenerAutorPorId(idAutor);
                            })
                    .map(autor -> autor.getNombre()+" "+autor.getApellido())
                    .distinct()
                    .collect(Collectors.joining(", "));

            // Devuelve el mapa con la estructura limpia que espera el fetch de JavaScript
            return Map.of(
                    "ejemplares", ejemplaresFiltrados,
                    "autores", nombresAutores
            );
        } catch (Exception e) {
            // En caso de caída, devolvemos listas vacías estructuradas para que el JS no rompa la interfaz
            return Map.of(
                    "ejemplares", List.of(),
                    "autores", "No se pudieron cargar los autores"
            );
        }
    }

    @PostMapping("/biblioteca/registrarLibro")
    public String guardarLibro(@ModelAttribute("libroNuevo") Libro libroNuevo, Model model, @RequestParam("idAutor") String idAutor) {
        try {
            ResponseEntity<Libro> response = libroRestClient.crearNuevoLibro(libroNuevo);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Armamos el objeto Escrito con los datos mínimos indispensables
                Autor autorCascaron = Autor.builder().idAutor(idAutor).build();
                Libro libroCascaron = Libro.builder()
                        .isbn(response.getBody().getIsbn())
                        .build();

                Escrito escritoNuevo = Escrito.builder()
                        .id(null)
                        .libro(libroCascaron)
                        .autor(autorCascaron)
                        .build();

                // 3. Enviamos el escrito al backend (que ahora recibe un @RequestBody Escrito)
                // El backend le asignará el ID (E001, E002...), la fecha, etc., y lo guardará.
                escritoFeignClient.agregarEscrito(escritoNuevo);
                return "redirect:/biblioteca/listar-libros";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo registrar el libro. Verifica los datos.");
            }

        } catch (Exception e) {
            model.addAttribute("errorMensaje", e.getMessage());
        }

        // --- ASEGURAMOS QUE TODAS LAS LISTAS VUELVAN AL HTML ---
        try {
            model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());
        } catch (Exception ignored) {}

        try {
            model.addAttribute("listaEditoriales", editorialFeignClient.obtenerEditoriales());
        } catch (Exception ignored) {}

        try {
            model.addAttribute("listaAutores", autorFeignClient.obtenerAutores()); // <--- ¡FALTABA ESTA!
        } catch (Exception ignored) {}

        try {
            model.addAttribute("listaEjemplares", ejemplarFeingClient.obtenerEjemplares()); // <--- ¡FALTABA ESTA!
        } catch (Exception ignored) {}

        model.addAttribute("abrirModal", true);
        return "libro/listar-libros";
    }

    @PostMapping("/biblioteca/actualizarLibro")
    public String libroActualizado(@ModelAttribute Libro libroActualizar, Model model) {
        try {
            ResponseEntity<Libro> response = libroRestClient.actualizarLibro(libroActualizar.getIsbn(), libroActualizar);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/listar-libros";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo actualizar el libro. Verifica los campos.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible en este momento.");
        }

        // Si falla la edición, reinyectamos las editoriales para que el modal de edición no se rompa
        model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());
        try {
            model.addAttribute("listaEditoriales", editorialFeignClient.obtenerEditoriales());
        } catch (Exception ignored) {}

        model.addAttribute("libroNuevo", Libro.builder().build());
        return "libro/listar-libros";
    }

    @GetMapping("/biblioteca/eliminarLibro/{isbn}")
    public String eliminarLibro(@PathVariable String isbn, Model model) {
        try {
            ResponseEntity<String> response = libroRestClient.eliminarLibro(isbn);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/listar-libros";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            }
            else if (response.getStatusCode() == HttpStatus.BAD_REQUEST)
            {
                model.addAttribute("errorMensaje", response.getBody());
            }
            else {
                model.addAttribute("errorMensaje", "Ha ocurrido un error al eliminar el libro. Intente más tarde");
            }
        } catch (Exception ignored) {
        }

        model.addAttribute("listaTodosLibros", libroRestClient.listarLibros());
        try {
            model.addAttribute("listaEditoriales", editorialFeignClient.obtenerEditoriales());
        } catch (Exception ignored) {}

        if (!model.containsAttribute("libroNuevo")) {
            model.addAttribute("libroNuevo", Libro.builder().build());
        }
        return "libro/listar-libros";
    }
}