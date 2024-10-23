package api.controller;

import api.entity.Comentario;
import api.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Comentario> salvarComentario(@RequestBody Comentario comentario) {
        Comentario novoComentario = comentarioService.salvarComentario(comentario);
        return ResponseEntity.ok(novoComentario);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Comentario>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listarComentarios());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Comentario>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.buscarPorId(id));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirComentario(@PathVariable Long id) {
        comentarioService.excluirComentario(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Comentario>> listarComentariosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorUsuario(idUsuario));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/postagem/{idPostagem}")
    public ResponseEntity<List<Comentario>> listarComentariosPorPostagem(@PathVariable Long idPostagem) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorPostagem(idPostagem));
    }
}
