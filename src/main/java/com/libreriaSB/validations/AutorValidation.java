package com.libreriaSB.validations;

import com.libreriaSB.exceptions.MyException;

public class AutorValidation {

    public static void validarNombre(String nombre) throws MyException {
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
}