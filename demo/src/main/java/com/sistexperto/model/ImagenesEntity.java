
package com.sistexperto.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SE_IMAGENES_CLASIFICADAS")
public class ImagenesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SE_IMAGEN")
    private Long id;

    @Column(name = "RUTA_IMAGEN", nullable = false)
    private String ruta_imagen;

    @Column(name = "NOMBRE_IMAGEN", nullable = false)
    private String nombre_imagen;
}
