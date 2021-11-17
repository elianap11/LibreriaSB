package com.libreriaSB.services;

import com.libreriaSB.entities.Rol;
import com.libreriaSB.entities.Usuario;
import com.libreriaSB.repositories.UsuarioRepository;
import java.util.Collections;
import java.util.List;
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

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final String mensaje = "No existe un usuario registrado con el correo %s";

    @Transactional
    public void crear(String id, String nombre, String apellido, String correo, String clave, Rol rol) throws Exception {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new Exception("Ya existe un usuario asociado con el correo ingresado");
        }

        Usuario usuario = new Usuario();

        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setClave(encoder.encode(clave));
        //validación
//        if (usuarioRepository.findAll().isEmpty()) {
//            usuario.setRol(rol.setNombre(ADMIN));
//        } else if (usuario.getRol() == null) {
//            usuario.setRol(rol.setNombre(USER));
//        } else {
//            usuario.setRol(rol.getNombre());
//        }

        usuario.setAlta(true);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String correo, String clave, Rol rol) {
        usuarioRepository.modificar(id, nombre, apellido, correo, clave, rol);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios() {
        return usuarioRepository.findAll();
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
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(mensaje, correo)));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }
}
