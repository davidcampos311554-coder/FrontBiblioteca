package com.frontend.frontbiblioteca.Views;

import com.frontend.frontbiblioteca.Model.Miembro;
import com.frontend.frontbiblioteca.WebServiceClient.MiembroFeingClient;
import lombok.RequiredArgsConstructor;
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
    public String guardarMiembro(@ModelAttribute("miembroNuevo") Miembro miembroNuevo, Model model) {
        try {
            Miembro response = miembroFeingClient.crearMiembro(miembroNuevo);

            if (response != null) {
                return "redirect:/biblioteca/miembros/listar";
            } else {
                model.addAttribute("errorMensaje", "No se pudo registrar el miembro. Verifica los datos.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible.");
        }

        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception ignored) {}

        model.addAttribute("abrirModal", true);
        return "miembro/listar-miembros";
    }

    @PostMapping("/biblioteca/actualizarMiembro")
    public String miembroActualizado(@ModelAttribute Miembro miembroActualizar, Model model) {
        try {
            Miembro response = miembroFeingClient.actualizarMiembro(miembroActualizar.getIdMiembro(), miembroActualizar);

            if (response != null) {
                return "redirect:/biblioteca/miembros/listar";
            } else {
                model.addAttribute("errorMensaje", "No se pudo actualizar el miembro. Verifica los campos.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible en este momento.");
        }

        try {
            model.addAttribute("listaTodosMiembros", miembroFeingClient.listarTodosMiembros());
        } catch (Exception ignored) {}

        model.addAttribute("miembroNuevo", Miembro.builder().build());
        return "miembro/listar-miembros";
    }

    @GetMapping("/biblioteca/eliminarMiembro/{idMiembro}")
    public String cambiarEstadoMiembro(@PathVariable String idMiembro, Model model) {
        try {
            miembroFeingClient.eliminarMiembro(idMiembro);
            return "redirect:/biblioteca/miembros/listar";
        } catch (Exception e) {
            model.addAttribute("errorMensaje", "El servicio de backend no está disponible o no se pudo eliminar.");
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