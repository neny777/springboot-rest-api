package br.com.supermidia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/send-reset-code")
    public ResponseEntity<String> sendResetCode(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
    	
        passwordService.sendResetCode(emailRequestDTO.getEmail());
        return ResponseEntity.ok("Código de redefinição enviado para o e-mail informado.");
    }

    @PostMapping("/validate-reset-code")
    public ResponseEntity<String> validateResetCode(@RequestBody @Valid CodeValidationRequestDTO codeRequestDTO) {
        passwordService.validateCode(codeRequestDTO.getEmail(), codeRequestDTO.getCode());
        return ResponseEntity.ok("Código validado com sucesso.");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid ResetPasswordRequestDTO passwordRequestDTO) {
        passwordService.resetPassword(passwordRequestDTO.getEmail(), passwordRequestDTO.getNewPassword());
        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}

