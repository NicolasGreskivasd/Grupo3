package api.repository;

import api.entity.Log;
import api.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    // Buscar logs por id do usuário
    List<Log> findByIdUsuario(Long idUsuario);

    // Buscar logs por ação
    List<Log> findByAcao(String acao);
}