package com.libreriaSB.controllers;

import com.libreriaSB.entities.Autor;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.AutorService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public ModelAndView mostrarAutores(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("autores");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("autores", autorService.buscarAutores());
        return mav;
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")   
    public ModelAndView crearAutor() {
        ModelAndView mav = new ModelAndView("autor-formulario");
        mav.addObject("autor", new Autor());
        mav.addObject("title", "Crear Autor");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarAutor(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("autor-formulario");
        mav.addObject("autor", autorService.buscarAutorPorId(id));
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes redirectAttributes) throws MyException {
        try {
            autorService.crearAutor(nombre);
            redirectAttributes.addFlashAttribute("exito-name", "El autor se guardó exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
            return new RedirectView("/autores/crear");
        }
        return new RedirectView("/autores");
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes redirectAttributes) throws MyException {
        try {
            autorService.modificarAutor(id, nombre);
            redirectAttributes.addFlashAttribute("exito-name", "El autor fue modificado exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/autores");
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView eliminar(@PathVariable String id) {
        autorService.eliminarAutor(id);
        return new RedirectView("/autores");
    }

    @PostMapping("/alta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView alta(@PathVariable String id) {
        autorService.alta(id);
        return new RedirectView("/autores");
    }
}