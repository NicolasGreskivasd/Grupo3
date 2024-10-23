package api.repository;

import api.entity.Feedback;
import api.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Buscar feedbacks por id do usuário
    List<Feedback> findByIdUsuario(Long idUsuario);
}