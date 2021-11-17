package com.libreriaSB.controllers;

import com.libreriaSB.entities.Editorial;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.EditorialService;
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
@RequestMapping("/editoriales")
public class EditorialController {
    
    @Autowired
    private EditorialService editorialService;

    @GetMapping
    public ModelAndView mostrarEditoriales(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("editoriales");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("editoriales", editorialService.buscarEditoriales());
        return mav;
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crearEditorial() {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarEditorial(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", editorialService.buscarEditorialPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes redirectAttributes) throws MyException {
        try {
            editorialService.crearEditorial(nombre);
            redirectAttributes.addFlashAttribute("exito-name", "La editorial se guardó exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
            return new RedirectView("/editoriales/crear");
        }
        return new RedirectView("/editoriales");
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes redirectAttributes) throws MyException{
        try {
            editorialService.modificarEditorial(id, nombre);
            redirectAttributes.addFlashAttribute("exito-name", "La editorial fue modificada exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/editoriales");
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView eliminar(@PathVariable String id) {
        editorialService.eliminarEditorial(id);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/alta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView alta(@PathVariable String id) {
        editorialService.alta(id);
        return new RedirectView("/editoriales");
    }
}