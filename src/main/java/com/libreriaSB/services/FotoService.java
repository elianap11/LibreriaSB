package com.libreriaSB.services;

import com.libreriaSB.exceptions.MyException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {

@Value("${my.property}")        
private String directory;

    public String copiar(MultipartFile archivo) throws MyException, IOException {
        try {
            String nombreFoto = archivo.getOriginalFilename();
            Path rutaFoto = Paths.get(directory, nombreFoto).toAbsolutePath();
//Files.copy copia la foto, es un método estático
//este método getInputStream genera una excepción y dice: si ya existe, no lo copies
            Files.copy(archivo.getInputStream(), rutaFoto, StandardCopyOption.REPLACE_EXISTING);
            return nombreFoto;
        } catch (IOException e) {
            throw new MyException("Error al guardar la foto");
        }
    }

}
