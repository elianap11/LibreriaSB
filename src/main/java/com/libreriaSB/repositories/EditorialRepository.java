package com.libreriaSB.repositories;

import com.libreriaSB.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, String> {

    @Modifying
    @Query("UPDATE Editorial e SET e.nombre = :nombre WHERE e.id = :id")
    void modificarEditorial(@Param("id") String id, @Param("nombre") String nombre);
    
    @Modifying
    @Query("UPDATE Editorial e SET e.alta = true WHERE e.id = :id")
    void darDeAlta(@Param("id") String id);

    @Query("FROM Editorial e WHERE e.nombre = :nombre")
    Editorial buscarNombre(@Param("nombre") String nombre);
    
}
