package api.controller;

import api.entity.Log;
import api.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Log> salvarLog(@RequestBody Log log) {
        Log novoLog = logService.salvarLog(log);
        return ResponseEntity.ok(novoLog);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Log>> listarLogs() {
        return ResponseEntity.ok(logService.listarLogs());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Log>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(logService.buscarPorId(id));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Log>> listarLogsPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(logService.listarLogsPorUsuario(idUsuario));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<Log>> listarLogsPorAcao(@PathVariable String acao) {
        return ResponseEntity.ok(logService.listarLogsPorAcao(acao));
    }
}