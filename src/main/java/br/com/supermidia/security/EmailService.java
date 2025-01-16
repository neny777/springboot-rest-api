package br.com.supermidia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Envia um e-mail simples com assunto e corpo.
	 * 
	 * @param to      O endereço de e-mail do destinatário.
	 * @param subject O assunto do e-mail.
	 * @param text    O corpo do e-mail.
	 */
	public void sendEmail(String to, String subject, String text) {
		
		

		try {
			
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			message.setFrom("suporte@supermidiaalfenas.com.br"); // Opcional: personalizar o remetente
			
			mailSender.send(message);
			
			
		} catch (Exception e) {
			System.err.println("Erro ao enviar e-mail: " + e.getMessage());
			throw new RuntimeException("Erro ao enviar e-mail.", e);
		}
	}
}
