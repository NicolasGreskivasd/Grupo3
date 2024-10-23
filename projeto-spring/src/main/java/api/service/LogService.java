package api.service;

import api.entity.Log;
import api.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Log salvarLog(Log log) {
        return logRepository.save(log);
    }

    public List<Log> listarLogs() {
        return logRepository.findAll();
    }

    public Optional<Log> buscarPorId(Long id) {
        return logRepository.findById(id);
    }

    public List<Log> listarLogsPorUsuario(Long idUsuario) {
        return logRepository.findByIdUsuario(idUsuario);
    }

    public List<Log> listarLogsPorAcao(String acao) {
        return logRepository.findByAcao(acao);
    }
}