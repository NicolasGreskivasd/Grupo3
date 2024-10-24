package api.controller;

import api.entity.Postagem;
import api.service.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Postagem>> listarPostagens() {
        List<Postagem> postagens = postagemService.obterTodasPostagens();
        return ResponseEntity.ok(postagens);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/adicionar")
    public ResponseEntity<Postagem> adicionarPostagem(@RequestBody Postagem postagem) {
        Postagem novaPostagem = postagemService.salvarPostagem(postagem);
        return ResponseEntity.ok(novaPostagem);
    }
}