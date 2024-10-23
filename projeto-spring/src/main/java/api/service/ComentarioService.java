package api.service;

import api.entity.Comentario;
import api.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public Comentario salvarComentario(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    public List<Comentario> listarComentarios() {
        return comentarioRepository.findAll();
    }

    public Optional<Comentario> buscarPorId(Long id) {
        return comentarioRepository.findById(id);
    }

    public void excluirComentario(Long id) {
        comentarioRepository.deleteById(id);
    }

    public List<Comentario> listarComentariosPorUsuario(Long idUsuario) {
        return comentarioRepository.findByIdUsuario(idUsuario);
    }

    public List<Comentario> listarComentariosPorPostagem(Long idPostagem) {
        return comentarioRepository.findByIdPostagem(idPostagem);
    }
}