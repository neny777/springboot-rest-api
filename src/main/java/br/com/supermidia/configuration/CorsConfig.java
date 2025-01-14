package br.com.supermidia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    CorsFilter corsFilter() {
		System.out.println("Configuração de CORS carregada.");
		
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		// Permitir o domínio do frontend
		corsConfiguration.addAllowedOrigin("http://192.168.3.18:3000");

		// Permitir todos os métodos HTTP (GET, POST, etc.)
		corsConfiguration.addAllowedMethod("*");

		// Permitir todos os cabeçalhos (incluindo Authorization)
		corsConfiguration.addAllowedHeader("*");

		// Permitir envio de credenciais (opcional)
		corsConfiguration.setAllowCredentials(true);

		// Configurar fonte de CORS
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return new CorsFilter(source);
	}
}
