package br.com.supermidia.security;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.usuario.Usuario;
import br.com.supermidia.pessoa.usuario.UsuarioRepository;

@Service
public class PasswordService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, String> resetCodes = new ConcurrentHashMap<>();
    private Map<String, LocalDateTime> codeExpiry = new ConcurrentHashMap<>();
    private Map<String, Boolean> validatedEmails = new ConcurrentHashMap<>();

    public void sendResetCode(String email) {
        Usuario usuario = usuarioRepository.findByFisicaEmail(email).orElse(null);

        if (usuario != null) {
            String code = String.format("%06d", new Random().nextInt(999999));
            System.out.println("Código de redefinição de senha: " + code);

            resetCodes.put(email, code);
            codeExpiry.put(email, LocalDateTime.now().plusMinutes(10));

            emailService.sendEmail(email, "Código de Redefinição de Senha", "Seu código é: " + code);
        }
    }

    public void validateCode(String email, String code) {
        String savedCode = resetCodes.get(email);
        LocalDateTime expiry = codeExpiry.get(email);

        if (savedCode == null || expiry == null || expiry.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Código expirado ou inválido.");
        }

        if (!savedCode.equals(code)) {
            throw new IllegalArgumentException("Código inválido.");
        }

        // Marca o e-mail como validado
        validatedEmails.put(email, true);
    }

    public void resetPassword(String email, String newPassword) {
        // Verifica se o e-mail foi validado
        if (!validatedEmails.containsKey(email) || !validatedEmails.get(email)) {
            throw new IllegalArgumentException("Validação do código é obrigatório antes da redefinição da senha.");
        }

        // Verifica se o prazo do código ainda é válido
        LocalDateTime expiry = codeExpiry.get(email);
        if (expiry == null || expiry.isBefore(LocalDateTime.now())) {
            validatedEmails.remove(email); // Remove validação se o prazo expirar
            resetCodes.remove(email);
            codeExpiry.remove(email);
            throw new IllegalArgumentException("O prazo para redefinição da senha expirou. Solicite um novo código.");
        }

        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        Usuario usuario = usuarioRepository.findByFisicaEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        usuario.setSenha(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        // Limpa as informações de validação após a redefinição
        validatedEmails.remove(email);
        resetCodes.remove(email);
        codeExpiry.remove(email);
    }
}

