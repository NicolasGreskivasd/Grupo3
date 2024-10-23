package api.controller;

import api.entity.Perfil;
import api.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Perfil> salvarPerfil(@RequestBody Perfil perfil) {
        Perfil novoPerfil = perfilService.salvarPerfil(perfil);
        return ResponseEntity.ok(novoPerfil);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Perfil>> listarPerfis() {
        return ResponseEntity.ok(perfilService.listarPerfis());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Perfil>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.buscarPorId(id));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        perfilService.excluirPerfil(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Perfil>> listarPerfisPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(perfilService.listarPerfisPorUsuario(idUsuario));
    }
}