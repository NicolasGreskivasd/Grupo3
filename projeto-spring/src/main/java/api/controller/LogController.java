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
        Log novoLog = logService.registrarLog(log);
        return ResponseEntity.ok(novoLog);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Log>> listarLogs() {
        return ResponseEntity.ok(logService.obterTodosLogs());
    }

}