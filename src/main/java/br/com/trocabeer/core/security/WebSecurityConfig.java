package br.com.trocabeer.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests((authorize) -> authorize.requestMatchers("/").permitAll()
				.requestMatchers("/registro/**").permitAll().requestMatchers("/esqueci-senha/**").permitAll().requestMatchers("/politica").permitAll()
				.requestMatchers("/alterar-senha/**").permitAll().requestMatchers("/home").authenticated()
				.requestMatchers("/pesquisa/**").authenticated().requestMatchers("/cerveja/**").authenticated()
				.requestMatchers("/troca/**").authenticated().requestMatchers("/usuario/**").authenticated()
				.requestMatchers("/cervejas/**").hasRole("ADMIN").requestMatchers("/trocas/**").authenticated().requestMatchers("/avaliacoes/**").authenticated()
				.requestMatchers("/suporte/**").authenticated().requestMatchers("/ajax/**").permitAll())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/home", true).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
		http.headers().frameOptions().disable();
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**", "/scss/**",
				"/app/**", "/img/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}