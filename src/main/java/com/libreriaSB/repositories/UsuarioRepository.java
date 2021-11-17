package com.libreriaSB.repositories;

import com.libreriaSB.entities.Rol;
import com.libreriaSB.entities.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    
//    // Alternativa de actualización de datos a través de consulta
    @Modifying
    @Query("UPDATE Usuario u SET u.nombre = :nombre, u.apellido = :apellido, u.correo = :correo, u.clave = :clave, u.rol = :rol WHERE u.id = :id")
    void modificar(@Param("id") String id, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("correo") String correo, @Param("clave") String clave, @Param("rol") Rol rol);

//    // Creación de consulta a partir de JPQL
//    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
//    Usuario buscarPorCorreo(@Param("correo") String correo);
//
//    // Creación de consulta nativa
//    @Query(value = "SELECT * FROM usuario WHERE correo = ?1", nativeQuery = true)
//    Usuario buscarPorCorreoSQL(String correo);

    // Creación de consulta a partir del nombre de método
    Optional<Usuario> findByCorreo(String correo);

    // Creación de consulta a partir del nombre de método
    boolean existsByCorreo(String correo);
    
    @Modifying
    @Query("UPDATE Usuario l SET l.alta = true WHERE l.id = :id")
    void darDeAlta(@Param("id") String id);
}