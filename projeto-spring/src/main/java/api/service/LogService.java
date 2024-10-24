package api.service;

import api.entity.Log;
import api.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    // Método para registrar um log
    public Log registrarLog(Log log) {
        log.setDataAcao(new Date());
        return logRepository.save(log);
    }

    // Método para obter todos os logs
    public List<Log> obterTodosLogs() {
        return logRepository.findAll();
    }

}