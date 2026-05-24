package com.frontend.frontbiblioteca.Views;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeViewController {

    @GetMapping({"/", "/biblioteca/inicio"})
    public String inicio(Model model) {
        // Aquí en el futuro podrías mandar estadísticas al modelo
        model.addAttribute("tituloPagina", "Inicio - Sistema de Biblioteca");
        return "inicio"; // Renderiza inicio.html
    }
}