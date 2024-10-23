package api.service;

import api.entity.Usuario;
import api.model.UsuarioModel;
import api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para criptografar a senha usando SHA-256
    public String criptografarSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }

    // Lógica para cadastrar um novo usuário
    public Usuario cadastrarUsuario(UsuarioModel usuarioModel) {

        Usuario usuarioEntity = new Usuario();

        // Criptografa a senha antes de salvar
        String senhaCriptografada = criptografarSenha(usuarioModel.getSenha());
        usuarioEntity.setSenha(senhaCriptografada);

        usuarioEntity.setPerfil(usuarioModel.getPerfil());
        usuarioEntity.setNome(usuarioModel.getNome());
        usuarioEntity.setEmail(usuarioModel.getEmail());
        usuarioEntity.setDataCriacao(new Date());

        // Salva o novo usuário no banco de dados
        return usuarioRepository.save(usuarioEntity);
    }

    // Método para login, comparando as senhas
    public boolean login(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String senhaCriptografada = criptografarSenha(senha);

            // Compara a senha criptografada com a armazenada no banco
            return usuario.getSenha().equals(senhaCriptografada);
        }

        return false;
    }

    // Listar todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar um usuário por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }


    // Atualizar um usuário existente
    public Optional<Usuario> atualizarUsuario(Long id, UsuarioModel usuarioModel) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(usuarioModel.getNome());
            usuario.setEmail(usuarioModel.getEmail());
            if (!usuarioModel.getSenha().isEmpty()) {
                usuario.setSenha(criptografarSenha(usuarioModel.getSenha()));
            }
            usuario.setPerfil(usuarioModel.getPerfil());
            return usuarioRepository.save(usuario);
        });
    }

    // Excluir um usuário
    public boolean excluirUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioRepository.delete(usuario);
            return true;
        }).orElse(false);
    }
}