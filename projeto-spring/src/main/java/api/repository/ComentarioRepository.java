package api.repository;

import api.entity.Comentario;
import api.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    // Buscar comentários por id do usuário
    List<Comentario> findByIdUsuario(Long idUsuario);

    // Buscar comentários por id da postagem
    List<Comentario> findByIdPostagem(Long idPostagem);
}