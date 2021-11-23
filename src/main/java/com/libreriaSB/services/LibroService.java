package com.libreriaSB.services;

import com.libreriaSB.entities.Autor;
import com.libreriaSB.entities.Libro;
import com.libreriaSB.entities.Editorial;
import com.libreriaSB.exceptions.MyException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.libreriaSB.repositories.LibroRepository;
import static com.libreriaSB.validations.LibroValidation.validarAnio;
import static com.libreriaSB.validations.LibroValidation.validarIsbn;
import static com.libreriaSB.validations.LibroValidation.validarTitulo;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Autor autor, Editorial editorial) throws MyException {

               // validarIsbn(isbn);

        validarTitulo(titulo);
        validarAnio(anio);
        
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplaresRestantes);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(true);
        libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public Libro buscarLibroId(String id) {
        Optional<Libro> libroOptional = libroRepository.findById(id);
        return libroOptional.orElse(null);
    }
    
    @Transactional
    public void modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Autor autor, Editorial editorial) throws MyException {
        validarTitulo(titulo);
        validarAnio(anio);
       // validarIsbn(isbn);
        
        Libro libro = libroRepository.findById(id).orElse(null);
        if (libro == null) {
            throw new MyException("No existe el libro.");
        }

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplaresRestantes);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libroRepository.save(libro);

        // libroRepositorio.modificarLibro(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
    }

  

    @Transactional(readOnly = true)
    public List<Libro> buscarLibros() {
        return libroRepository.findAll();
    }
//
//      @Transactional
//    public void eliminarLibro(String id) {
//        libroRepositorio.eliminarLibro(id);
//    }

    @Transactional
    public void eliminarLibro(String id) {
        libroRepository.deleteById(id);
    }

    @Transactional
    public void alta(String id) {
        libroRepository.darDeAlta(id);
    }
}

