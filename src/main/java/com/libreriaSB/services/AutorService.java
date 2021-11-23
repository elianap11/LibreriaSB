package com.libreriaSB.services;

import com.libreriaSB.entities.Autor;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.validations.AutorValidation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.libreriaSB.repositories.AutorRepository;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository autorRepository;    
    
    private AutorValidation autorValidation;

    @Transactional
    public void crearAutor(String nombre) throws MyException{

        //autorValidation.validarNombre(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        autorRepository.save(autor);
    }

    @Transactional(readOnly = true)
    public Autor buscarAutorPorId(String id) {
        Optional<Autor> autorOptional = autorRepository.findById(id);
        return autorOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarAutores() {
        return autorRepository.findAll();
    }

    @Transactional
    public void modificarAutor(String id, String nombre) throws MyException {
        //autorValidation.validarNombre(nombre);
        Autor autor = autorRepository.findById(id).orElse(null);
        if (autor == null) {
            throw new MyException("No existe el autor.");
        }

        autor.setNombre(nombre);
        autorRepository.save(autor);

        //autorRepositorio.modificarAutor(id, nombre);
    }

//    @Transactional
//    public void eliminarAutor(String id) {
//        autorRepositorio.eliminarAutor(id);
//  }
    
//Uso el deleteById con la anotación en la entidad  
    @Transactional
    public void eliminarAutor(String id) {
        autorRepository.deleteById(id);
    }

    @Transactional
    public void alta(String id) {
        autorRepository.darAlta(id);
    }

}
  