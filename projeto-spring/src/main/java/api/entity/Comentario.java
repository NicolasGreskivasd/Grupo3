package api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;


    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_postagem", nullable = false)
    private Long idPostagem;

    @Column(nullable = false)
    private String comentario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Timestamp dataCriacao;
}