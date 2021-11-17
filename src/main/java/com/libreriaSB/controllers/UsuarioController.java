package com.libreriaSB.controllers;

import com.libreriaSB.entities.Rol;
import com.libreriaSB.entities.Usuario;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.RolService;
import com.libreriaSB.services.UsuarioService;
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
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @GetMapping
    public ModelAndView mostrarUsuarios(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("usuario", usuarioService.buscarUsuarios());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crear(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }

        mav.addObject("title", "Crear Usuario");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable String id, HttpServletRequest request, RedirectAttributes attributes) {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("usuario", flashMap.get("usuario"));
            } else {
                mav.addObject("usuario", usuarioService.buscarPorCorreo(id));
            }

            mav.addObject("title", "Editar Usuario");
            mav.addObject("action", "modificar");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/usuarios");
        }
        return mav;
    }

    @PostMapping("/guardar")
    //con el ModelAttribute le paso el usuario entero, sin poner atributo por atributo
    public RedirectView guardar(@RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, @RequestParam Rol rol, RedirectAttributes attributes) throws Exception {
        RedirectView redirectView = new RedirectView("usuarios");
        try {
            usuarioService.crear(id, nombre, apellido, correo, clave, rol);
            attributes.addFlashAttribute("exito", "El usuario se guardó exitosamente");
        } catch (MyException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/usuarios/crear");
        }
        return redirectView;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, @RequestParam Rol rol, RedirectAttributes attributes) throws MyException{
        RedirectView redirectView = new RedirectView("usuarios");
        usuarioService.modificar(id, nombre, apellido, correo, clave, rol);
        attributes.addFlashAttribute("exito", "El usuario fue modificado exitosamente.");
        return redirectView;
    }

    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) {
        usuarioService.habilitar(id);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return new RedirectView("/usuarios");
    }
}
