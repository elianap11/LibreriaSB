package com.libreriaSB.services;

import com.libreriaSB.entities.Editorial;
import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.repositories.EditorialRepository;
import com.libreriaSB.validations.EditorialValidation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {
    
    @Autowired
    private EditorialRepository editorialRepository;

    private EditorialValidation editorialValidation;

    @Transactional
    public void crearEditorial(String nombre) throws MyException {
        
        //EditorialValidation editorialValidation = new EditorialValidation();    
        editorialValidation.validarNombre(nombre);
        editorialValidation.validarRepetido(nombre);
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        editorialRepository.save(editorial);
    }

    @Transactional(readOnly = true)
    public Editorial buscarEditorialPorId(String id) {
        Optional<Editorial> editorialOptional = editorialRepository.findById(id);
        return editorialOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Editorial> buscarEditoriales() {
        return editorialRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorNombre(String nombre){
        Editorial editorialOptional = editorialRepository.buscarNombre(nombre);
        return editorialOptional;
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws MyException {
        editorialValidation.validarNombre(nombre);
        editorialValidation.validarRepetido(nombre);

        Editorial editorial = editorialRepository.findById(id).orElse(null);
        if(editorial == null){
            throw new MyException("No existe la editorial.");
        }
        
        editorial.setNombre(nombre);
        editorialRepository.save(editorial);
        
       // editorialRepositorio.modificarEditorial(id, nombre);
    }

//    @Transactional
//    public void eliminarEditorial(String id){
//        editorialRepositorio.eliminarEditorial(id);       
//        
//    }
    
    //Uso el deleteById con la anotación en la entidad  
    
    @Transactional
    public void eliminarEditorial(String id) {
        editorialRepository.deleteById(id);
    }
    
   @Transactional
    public void alta(String id) {
        editorialRepository.darDeAlta(id);
    }
  
}
