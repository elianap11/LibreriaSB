package com.libreriaSB.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE Usuario SET alta = false WHERE id = ?")
/* Es una auditoría de entidades (es de JPA) Hace un seguimiento
entre la fecha de creación y la de modificación. se habilita en el main 
con @EnableJpaAuditing*/
//@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
   
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false, unique = true)
    private String correo;
    
    @Column(nullable = false)
    private String clave;
    
//    @CreatedDate //crea la fecha inicial que no debe ser modificada
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime fechaCreacion;
//    
//    @LastModifiedDate //permite que se modifique la fecha
//    private LocalDateTime fechaModificacion;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Rol rol;
   
    private Boolean alta; 

    public Usuario() {
    }

    public Usuario(String id, String nombre, String apellido, String correo, String clave, Boolean alta) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.clave = clave;
        this.alta = alta;
    }
     
    
}
