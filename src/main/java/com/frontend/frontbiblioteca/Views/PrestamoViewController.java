package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.*;
import com.frontend.frontbiblioteca.WebServiceClient.BibliotecarioFeignClient;
import com.frontend.frontbiblioteca.WebServiceClient.EjemplarFeingClient;
import com.frontend.frontbiblioteca.WebServiceClient.MiembroFeingClient;
import com.frontend.frontbiblioteca.WebServiceClient.PrestamoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrestamoViewController {
    private final PrestamoFeignClient prestamoFeignClient; //Gestión de prestamos
    private final EjemplarFeingClient ejemplarFeingClient; // Para saber qué se llevan
    private final MiembroFeingClient miembroFeingClient; // Para saber quién lleva
    private final BibliotecarioFeignClient bibliotecarioFeignClient; // Quien realizó el prestamo

    //Pantalla Principal: Listar prestamos y preparar los ejemplares
    @GetMapping("/biblioteca/prestamos/listar")
    public String getPrestamos(Model model) {
        try{
            model.addAttribute("listaPrestamos", prestamoFeignClient.listarPrestamos());

            //Información
            model.addAttribute("listaMiembros", miembroFeingClient.listarTodosMiembros());
            model.addAttribute("listaBibliotecarios", bibliotecarioFeignClient.obtenerBibliotecarios());

            List<Ejemplar> ejemplaresDisponibles = ejemplarFeingClient.obtenerEjemplares()
                    .stream()
                    .filter(ejemplar -> Boolean.TRUE.equals(ejemplar.getEstado())) // Solo pasan los que tengan estado == true (1)
                    .toList();

            // Mandamos al HTML únicamente los que se pueden prestar en este momento
            model.addAttribute("listaEjemplaresDisponibles", ejemplaresDisponibles);
        }
        catch(Exception ex){
            model.addAttribute("errorMensaje", "Hubo un error al conectar con los servicios Backend");
        }

        //Creamos un objeto vacío para enlanzarlo al formulario HTML (th:object)
        if (!model.containsAttribute("prestamoNuevo")) {
            model.addAttribute("prestamoNuevo", Prestamo.builder().build());
        }

        return "prestamo/lista-prestamos";
    }

    @PostMapping("biblioteca/prestamo/guardar")
    public String guardarPrestamo(Model model,
                                  @RequestParam("idMiembro") String idMiembro,
                                  @RequestParam("idEjemplar") String idEjemplar,
                                  @RequestParam("idBibliotecario") String idBibliotecario) {
        try {
            //Creamos cascarones
            Miembro miembroCascaron = Miembro.builder().idMiembro(idMiembro).build();
            Ejemplar ejemplarCascaron = Ejemplar.builder().id(idEjemplar).estado(false).build();
            Bibliotecario biblioCascaron = Bibliotecario.builder().idBibliotecario(idBibliotecario).build();

            Prestamo prestamoCreado = Prestamo.builder()
                    .idPrestamo(null)
                    .miembro(miembroCascaron)
                    .ejemplar(ejemplarCascaron)
                    .bibliotecario(biblioCascaron)
                    .build();
            ResponseEntity<Prestamo> response = prestamoFeignClient.registrarNuevoPrestamo(prestamoCreado);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/prestamos/listar";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo registrar el prestamo. Verifica los datos.");
            }
        } catch (Exception ex) {
            model.addAttribute("errorMensaje", ex.getMessage());
        }

        cargarListasMapeo(model);

        model.addAttribute("abrirModal", true);

        return  "prestamo/lista-prestamos";
    }

    @PostMapping("/biblioteca/prestamo/actualizar")
    public String actualizarPrestamo(@RequestParam("idPrestamo") String idPrestamo,
                                     @RequestParam("fechaDevolucion") String fechaDevolucion,
                                     Model model) {
        try {
            Prestamo prestamoToUpdate = prestamoFeignClient.buscarPrestamoPorId(idPrestamo);

            if (prestamoToUpdate != null) {
                // Seteamos la fecha de devolución (hoy)
                prestamoToUpdate.setFechaDevolucion(java.time.LocalDate.parse(fechaDevolucion));

                // Enviamos la actualización al backend
                ResponseEntity<Prestamo> response = prestamoFeignClient.actualizarPrestamo(idPrestamo, prestamoToUpdate);

                if (response.getStatusCode().is2xxSuccessful()) {
                    // Redirección limpia a tu lista de préstamos
                    return "redirect:/biblioteca/prestamos/listar";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible o el préstamo no existe.");
        }

        // Si algo falla, recargamos las listas para que no se rompa la vista
        cargarListasMapeo(model);

        if (!model.containsAttribute("prestamoNuevo")) {
            model.addAttribute("prestamoNuevo", Prestamo.builder().build());
        }

        return "prestamo/lista-prestamos";
    }

    private void cargarListasMapeo(Model model) {
        try { model.addAttribute("listaPrestamos", prestamoFeignClient.listarPrestamos()); } catch (Exception ignored) {}
        try { model.addAttribute("listaEjemplares", ejemplarFeingClient.obtenerEjemplares()); } catch (Exception ignored) {}
        try { model.addAttribute("listaMiembros", miembroFeingClient.listarTodosMiembros()); } catch (Exception ignored) {}
        try { model.addAttribute("listaBibliotecarios", bibliotecarioFeignClient.obtenerBibliotecarios()); } catch (Exception ignored) {}
    }
}
