package com.libreriaSB.validations;

import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.repositories.EditorialRepository;

public class EditorialValidation {
    
    private EditorialRepository editorialRepository;

    public void validarNombre(String nombre) throws MyException {
        if (nombre == null) {
            throw new MyException("Ingrese el nombre");
        } else if (nombre.trim().isEmpty()) {
            throw new MyException("Debe ingresar el nombre");
        } else if (nombre.matches("^-?[0-9]+$")) {
            throw new MyException("El nombre debe ser ingresado con letras.");
        } else if (nombre.length() < 2) {
            throw new MyException("Nombre inválido, debe tener más de una letra");
        }
    }

    public void validarRepetido(String nombre) throws MyException {
        if (editorialRepository.buscarNombre(nombre).getAlta() == false) {
            throw new MyException("Ya existe el nombre de la editorial");
        }
    }
}

/*regex validar nombre con letras e inicio de mayúsculas
            public boolean valida(Alumno alumno) {
            String nombre = alumno.getNombre();
            return nombre.matches("\[A-Z\]\[a-z\]{1,}");
            
        //validar que el nombre de la editorial no sea igual a otro

        
    /*Para comprobar si un carácter es una letra, podemos usar el método 
           estático isAlphabetic() de la clase Character: 
        Boolean letras = false;
        for (int i = 0; i < nombre.length(); i++) {
            if (Character.isAlphabetic((nombre.charAt(i)))) {
                letras = true;
            }
        }

        if (letras == false) {
            throw new MyException("El nombre debe ser ingresado con letras.");
        }
 */
