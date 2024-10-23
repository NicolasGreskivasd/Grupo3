package api.repository;

import api.entity.Perfil;
import api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    List<Perfil> findByIdUsuario(Long idUsuario);
}