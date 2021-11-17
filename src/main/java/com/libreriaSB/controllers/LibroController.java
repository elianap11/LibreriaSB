package com.libreriaSB.controllers;

import com.libreriaSB.entities.Autor;
import com.libreriaSB.entities.Libro;
import com.libreriaSB.entities.Editorial;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.AutorService;
import com.libreriaSB.services.LibroService;
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
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private EditorialService editorialService;
    
    @GetMapping
    public ModelAndView mostrarLibros(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("libros");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("libros", libroService.buscarLibros());
        return mav;
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crearLibro() {
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", new Libro());
        mav.addObject("autores", autorService.buscarAutores());
        mav.addObject("editoriales", editorialService.buscarEditoriales());
        mav.addObject("title", "Crear Libro");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarLibro(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", libroService.buscarLibroId(id));
        mav.addObject("autores", autorService.buscarAutores());
        mav.addObject("editoriales", editorialService.buscarEditoriales());
        mav.addObject("title", "Editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @RequestParam Autor autor, @RequestParam Editorial editorial, RedirectAttributes redirectAttributes) throws MyException {
        try {
            libroService.crearLibro(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
            redirectAttributes.addFlashAttribute("exito-name", "El libro se guardó exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
            return new RedirectView("/libros/crear");
        }
        return new RedirectView("/libros");
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView modificar(@RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @RequestParam Autor autor, @RequestParam Editorial editorial, RedirectAttributes redirectAttributes) throws MyException {
        try {
            libroService.modificarLibro(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
            redirectAttributes.addFlashAttribute("exito-name", "El libro fue modificado exitosamente.");
        } catch (MyException e) {
            redirectAttributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/libros");
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView eliminar(@PathVariable String id) {
        libroService.eliminarLibro(id);
        return new RedirectView("/libros");
    }

    @PostMapping("/alta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView alta(@PathVariable String id) {
        libroService.alta(id);
        return new RedirectView("/libros");
    }
}