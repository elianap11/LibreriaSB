package com.libreriaSB.controllers;

import com.libreriaSB.entities.Usuario;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.FotoService;
import com.libreriaSB.services.UsuarioService;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
//@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private FotoService fotoService;
    
    private Usuario usuario;
    
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //login recibe tres posibles situaciones: cuando se genera un error, cuando se desloguea y recibir el principal para saber si está logueado el usuario
    //login viene de Security, del loginpage, es la ruta, no un html, lo que mapeamos.
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView mav = new ModelAndView("login");
        if (error != null) {
            mav.addObject("error", "CORREO O CONTRASEÑA INCORRECTO");
        }
        if (logout != null) {
            mav.addObject("logout", "Sesión Finalizada");
        }
        if (principal != null) {
            //imprimimos el correo
            LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }
        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {

        ModelAndView mav = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("nombre", flashMap.get("nombre"));
            mav.addObject("apellido", flashMap.get("apellido"));
            mav.addObject("correo", flashMap.get("correo"));
            mav.addObject("clave", flashMap.get("clave"));
            mav.addObject("image", flashMap.get("image"));
        }

        if (principal != null) {
            LOGGER.info("Principal -> {}", principal.getName());
            mav.setViewName("redirect:/");
        }
        return mav;
    }

    @PostMapping("/registro")
    public RedirectView signup(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, String image, MultipartFile foto, RedirectAttributes attributes, HttpServletRequest request) throws Exception {

        RedirectView redirectView = new RedirectView("/login");

        usuarioService.crear(nombre, apellido, correo, clave, image, foto);
        attributes.addFlashAttribute("exito", "SE HA REGISTRADO CON ÉXITO.");
        request.login(correo, clave);
        redirectView.setUrl("/index");
        return redirectView;
    }

    @GetMapping("/usuarios")
    public ModelAndView mostrarTodos(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("usuarios", usuarioService.buscarUsuarios());
        return mav;
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crearUsuario(HttpServletRequest request, MultipartFile foto) throws MyException, IOException {

        ModelAndView mav = new ModelAndView("usuario-formulario");

        if (!foto.isEmpty()) {
            usuario.setImage(fotoService.copiar(foto));
        }
        
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
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editarUsuario(@PathVariable String id, HttpSession session) {
        //Si el id coincide, vuelve a index logueado
        if (!session.getAttribute("id").equals(id)) {
            return new ModelAndView(new RedirectView("/index"));
        }
        ModelAndView mav = new ModelAndView("usuario-formulario");
        mav.addObject("usuario", usuarioService.buscarPorId(id));
        mav.addObject("title", "Editar Usuario");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView guardar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, @RequestParam String image, @RequestParam MultipartFile foto, RedirectAttributes attributes) throws MyException, Exception {
        
        try {
            usuarioService.crear(nombre, apellido, correo, clave, image, foto);
            attributes.addFlashAttribute("exito", "USUARIO CREADO CORRECTAMENTE");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/usuarios");

//        RedirectView redirectView = new RedirectView("/usuarios");
//
//        usuarioService.crear(nombre, apellido, correo, clave, image, foto);
//        attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");

//        return redirectView;
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView modificar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, String image, @RequestParam MultipartFile foto, RedirectAttributes attributes) throws Exception {
        if (!foto.isEmpty()) {
            usuario.setImage(fotoService.copiar(foto));
        }
        usuarioService.crear(nombre, apellido, correo, clave, image, foto);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/habilitar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView habilitar(@PathVariable String id) {
        usuarioService.habilitar(id);
        return new RedirectView("/usuarios");
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return new RedirectView("/usuarios");
    }
}
