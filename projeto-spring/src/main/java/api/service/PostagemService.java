package api.service;

import api.entity.Log;
import api.entity.Postagem;
import api.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private LogService logService;

    // Método para listar todas as postagens
    public List<Postagem> obterTodasPostagens() {
        return postagemRepository.findAll();
    }

    // Método para salvar uma nova postagem
    public Postagem salvarPostagem(Postagem postagem) {
        Log log = new Log();
        log.setIpUsuario("");
        log.setIdUsuario(1L);
        log.setAcao("Postagem");
        log.setDataAcao(new Date());
        logService.registrarLog(log);
        return postagemRepository.save(postagem);
    }

}
