package com.libreriaSB.controllers;

import com.libreriaSB.entities.Usuario;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.UsuarioService;
import java.security.Principal;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //login recibe tres posibles situaciones: cuando se genera un error, cuando se desloguea y recibir el principal para saber si está logueado el usuario
    //login viene de Security, del loginpage, es la ruta, no un html, lo que mapeamos.
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {
        ModelAndView mav = new ModelAndView("login");

        if (error != null) {
            mav.addObject("error", "Correo o contraseña inválida");
        }

        if (logout != null) {
            mav.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            //imprimimos el correo
            LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/home");
        }

        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView mav = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (principal != null) {
            mav.setViewName("redirect:/home");
        }

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }
        return mav;
    }

    @PostMapping("/registro")
    public RedirectView signup(@ModelAttribute Usuario usuario, HttpServletRequest request, RedirectAttributes attributes) throws MyException, Exception {
        RedirectView redirectView = new RedirectView("/login");
        try {
            usuarioService.crear(usuario.getId(), usuario.getNombre(),  usuario.getApellido(), usuario.getCorreo(), usuario.getClave(), usuario.getRol());
            //autologin
            request.login(usuario.getCorreo(), usuario.getClave());
        } catch (MyException e) {
            attributes.addFlashAttribute("usuario", new Usuario());
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/signup");
        } catch (ServletException e) {
            attributes.addFlashAttribute("error", "Error al realizar auto-login");
        }
        return redirectView;
    }
}