package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Miembro;
import com.frontend.frontbiblioteca.WebServiceClient.MiembroFeingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MiembroViewController {

    private final MiembroFeingClient miembroFeingClient;

    // PANTALLA ÚNICA: Lista los miembros según la ruta del menú
    @GetMapping("/biblioteca/miembros/listar")
    public String getMiembros(Model model) {
        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "Hubo un problemita al conectar con el servicio de miembros.");
        }

        if (!model.containsAttribute("miembroNuevo")) {
            model.addAttribute("miembroNuevo", Miembro.builder().build());
        }
        return "miembro/listar-miembros";
    }

    @PostMapping("/biblioteca/registrarMiembro")
    public String guardarMiembro(@ModelAttribute("miembroNuevo") Miembro miembro, Model model) {
        try {
            // Reconstrucción asegurando que no viaje ID nulo problemático
            // y mapeando TODOS los campos provenientes del formulario HTML
            Miembro miembroNuevo = Miembro.builder()
                    .idMiembro(null)
                    .nombre(miembro.getNombre())
                    .apellido(miembro.getApellido())
                    .correo(miembro.getCorreo())
                    .direccion(miembro.getDireccion())
                    .telefono(miembro.getTelefono())
                    .build();

            ResponseEntity<Miembro> response = miembroFeingClient.crearMiembro(miembroNuevo);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/miembros/listar";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo registrar el Miembro. Verifica los datos.");
            }

        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible.");
        }

        // Si falla, recargamos la lista para la tabla sin perder el estado del formulario que falló
        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception ignored) {}

        model.addAttribute("abrirModal", true); // Reabre el modal de registro automáticamente para mostrar el error
        return "miembro/listar-miembros";
    }

    @PostMapping("/biblioteca/actualizarMiembro")
    public String miembroActualizado(@ModelAttribute Miembro miembroActualizar, Model model) {
        try {
            ResponseEntity<Miembro> response = miembroFeingClient.actualizarMiembro(miembroActualizar.getIdMiembro(), miembroActualizar);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/miembros/listar";
            }

            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo actualizar el Miembro. Verifica los datos.");
            }

        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible.");
        }

        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception ignored) {}

        // Pasamos el mismo objeto que falló para que el modal refleje qué datos causaron el error
        model.addAttribute("miembroNuevo", miembroActualizar);
        return "miembro/listar-miembros";
    }

    @GetMapping("/biblioteca/eliminarMiembro/{idMiembro}")
    public String cambiarEstadoMiembro(@PathVariable String idMiembro, Model model) {
        try {
            ResponseEntity<Miembro> response = miembroFeingClient.eliminarMiembro(idMiembro);
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/biblioteca/miembros/listar";
            }
            if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                model.addAttribute("errorMensaje", "El servidor de base de datos está fuera de servicio.");
            } else {
                model.addAttribute("errorMensaje", "No se pudo eliminar el libro seleccionado.");
            }
        } catch (Exception e) {
            //Capturamos el mensaje de error para saber si es por prestamo activo o porque no se encontró
            String mensaje = (e.getMessage() != null && e.getMessage().contains("prestamos activos"))
                    ? "El miembro no puede ser eliminado porque tiene prestamos activos"
                    : "No se pudo eliminar el miembro. Verifique el estado del servicio.";

            model.addAttribute("errorMensaje", mensaje);
        }

        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception ignored) {}

        if (!model.containsAttribute("miembroNuevo")) {
            model.addAttribute("miembroNuevo", Miembro.builder().build());
        }
        return "miembro/listar-miembros";
    }
}