package api.service;

import api.entity.Perfil;
import api.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil salvarPerfil(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public List<Perfil> listarPerfis() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil> buscarPorId(Long id) {
        return perfilRepository.findById(id);
    }

    public void excluirPerfil(Long id) {
        perfilRepository.deleteById(id);
    }

    public List<Perfil> listarPerfisPorUsuario(Long idUsuario) {
        return perfilRepository.findByIdUsuario(idUsuario);
    }
}