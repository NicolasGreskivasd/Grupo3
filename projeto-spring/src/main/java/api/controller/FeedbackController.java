package api.controller;

import api.entity.Comentario;
import api.entity.Feedback;
import api.model.FeedbackModel;
import api.service.ComentarioService;
import api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Feedback> salvarFeedback(@RequestBody FeedbackModel feedback) {
        Feedback novoFeedback = feedbackService.salvarFeedback(feedback);
        return ResponseEntity.ok(novoFeedback);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Feedback>> listarFeedbacks() {
        return ResponseEntity.ok(feedbackService.listarFeedbacks());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Feedback>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.buscarPorId(id));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Feedback>> listarFeedbacksPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(feedbackService.listarFeedbacksPorUsuario(idUsuario));
    }
}
