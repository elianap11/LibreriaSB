package com.libreriaSB.services;

import com.libreriaSB.entities.Rol;
import com.libreriaSB.repositories.RolRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;

    @Transactional
    public void crear(String nombre) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rolRepository.save(rol);
    }

    @Transactional(readOnly = true)
    public List<Rol> buscarTodos() {
        return rolRepository.findAll();
    }
    
}
