 package br.com.medcare.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.medcare.services.UserRepository;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = false, securedEnabled = false, jsr250Enabled = true)
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer{

	   @Autowired private UserRepository userRepo;
	    @Autowired private JwtTokenFilter jwtTokenFilter;
	   // @Autowired CorsConfigurationSource corsConfigurationSource;
	   
	    @Bean
	    public UserDetailsService userDetailsService() {
	        return new UserDetailsService() {
	             
	            @Override
	            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	                return userRepo.findByEmail(username)
	                        .orElseThrow(
	                                () -> new UsernameNotFoundException("User " + username + " not found"));
	            }
	        };
	    }
	     
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	     
	    @Bean
	    public AuthenticationManager authenticationManager(
	            AuthenticationConfiguration authConfig) throws Exception {
	        return authConfig.getAuthenticationManager();
	    }
	     
	    

	    @Bean
	    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    	http.csrf().disable();
	    	http.cors().disable();
	    	//http.cors().configurationSource(corsConfigurationSource);
	        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        http.authorizeHttpRequests(auth -> auth.requestMatchers("/paciente/listar-pacientes", "/internacao/paciente/**", "/medicamentos/listar", "/authenticate", "/docs/**", "/users", "/medicos", "/paciente/incluir", "/swagger-ui/**", "/v3/api-docs/**", "/v2/api-docs/**").permitAll().requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated());
	         http.exceptionHandling(exception -> exception.authenticationEntryPoint(  (request, response, ex) -> {
                 response.sendError(
                         HttpServletResponse.SC_UNAUTHORIZED,
                         ex.getMessage());}));
	         
	        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	         
	        
	        
	        return http.build();
	    }  
	    
	   
}       