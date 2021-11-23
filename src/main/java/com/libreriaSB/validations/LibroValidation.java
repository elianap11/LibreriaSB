package com.libreriaSB.validations;

import com.libreriaSB.exceptions.MyException;
import java.util.Calendar;
import com.libreriaSB.repositories.LibroRepository;

public class LibroValidation {

    private static LibroRepository libroRepository;

    public static void validarTitulo(String nombre) throws MyException {
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

    public static void validarAnio(Integer anio) throws MyException {
        Calendar actual = Calendar.getInstance();
        if (anio == null) {
            throw new MyException("Ingrese un año");
        } else if (anio.toString().length() < 4) {
            throw new MyException("Fecha inválida");
        } else if (anio > actual.get(Calendar.YEAR)) {
            throw new MyException("Ingrese un año correcto");
        }

    }

    public static void validarIsbn(Long isbn) throws MyException {
        if (isbn == null) {
            throw new MyException("Debe ingresar el ISBN");
            //La expresión regular indica que comience en 978, que sean números del 0 al 9 y que sigan 10 dígitos
        } else if (!isbn.toString().matches("^(978)[0-9]{10}$")) {
            throw new MyException("ISBN invalido, debe comenzar con 978 y tener 13 digítos");
        } else if (libroRepository.buscarIsbn(isbn) != null) {
            throw new MyException("No puede haber un libro con el mismo ISBN");
        }
    }
}
