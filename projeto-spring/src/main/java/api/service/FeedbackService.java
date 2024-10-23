package api.service;

import api.entity.Feedback;
import api.model.FeedbackModel;
import api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback salvarFeedback(FeedbackModel feedback) {
        Feedback feedbackentity = new Feedback();
        feedbackentity.setDataFeedback(new Date());
        feedbackentity.setComentario(feedback.getComentario());
        feedbackentity.setIdUsuario(feedback.getIdUsuario());

        return feedbackRepository.save(feedbackentity);
    }

    public List<Feedback> listarFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> buscarPorId(Long id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> listarFeedbacksPorUsuario(Long idUsuario) {
        return feedbackRepository.findByIdUsuario(idUsuario);
    }
}