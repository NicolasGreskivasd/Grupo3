package api.repository;

import api.entity.Postagem;
import api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

}