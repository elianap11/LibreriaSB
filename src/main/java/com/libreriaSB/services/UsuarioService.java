package com.libreriaSB.services;

import com.libreriaSB.entities.Usuario;
import com.libreriaSB.enums.Rol;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.repositories.UsuarioRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FotoService fotoService;

    private final String mensaje = "No existe un usuario registrado con el correo %s";

    @Transactional
    public void crear(String nombre, String apellido, String correo, String clave, String image, MultipartFile foto) throws Exception {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new Exception("Ya existe un usuario asociado con el correo ingresado");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setClave(encoder.encode(clave));
        if (usuarioRepository.findAll().isEmpty()) {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(Rol.USER);
        }

        if (!foto.isEmpty()) {
            usuario.setImage(fotoService.copiar(foto));
        }

        usuario.setAlta(true);

        usuarioRepository.save(usuario);
        emailService.enviarThread(correo);
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String correo, String clave, String image, MultipartFile foto) throws MyException, IOException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);
            usuario.setClave(clave);
            if (!foto.isEmpty()) {
                usuario.setImage(fotoService.copiar(foto));
            }
            usuarioRepository.save(usuario);
        } else {
            throw new MyException("NO SE ENCONTRÓ EL USUARIO SOLICITADO.");
        }

        //usuarioRepository.modificar(id, nombre, apellido, correo, clave, image);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @Transactional
    public void habilitar(String id) {
        usuarioRepository.darDeAlta(id);
    }

    @Transactional
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    //Este método entra en juego cuando el usuario se loguea
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        //chequea que el correo exista: permite el acceso o lanza una excepción
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(mensaje, correo)));
        //La palabra ROLE_ (es la forma que reconoce los roles Spring) concatenada con el rol y el nombre de ese rol
        //Acá genera los permisos y se los pasa al User
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        //El Servlet se castea
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        //Si la sesión no está creada, con true la va a crear
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("id", usuario.getId());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("apellido", usuario.getApellido());
        session.setAttribute("rol", usuario.getRol().name());
        session.setAttribute("image", usuario.getImage());

        //le paso las autorizaciones en el collections
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }
}
