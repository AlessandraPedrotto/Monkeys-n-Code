package com.monkeysncode.config;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.monkeysncode.entites.Role;
import com.monkeysncode.entites.User;
import com.monkeysncode.repos.UserDAO;
import com.monkeysncode.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	UserDAO userDAO;

    private final UserService serviceUser;

    public SecurityConfig(@Lazy UserService serviceUser) { // Lazy used to bypass circular reference between userservice and securityconfig
        this.serviceUser = serviceUser;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Pass encoder
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/","/logo.png", "/login", "/register", "/style/**", "/script/**","/image/**","/traduzioni/**").permitAll();  // Accesso libero a home, login, registrazione e risorse statiche
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();  // All other pages require auth
                })
                .formLogin(form -> form
                    .loginPage("/")  // Define your custom login page
                    .defaultSuccessUrl("/", true)  // Redirect to form based login
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")  // Sets email as username
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler())
                    .permitAll()
                )
                .oauth2Login(oauth -> oauth
                    .loginPage("/login")
                    .successHandler(oAuth2AuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)  
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null) {
                            	User user=serviceUser.userCheck(authentication.getPrincipal());
                            	if(user!=null) {
                            		user.setOnline(false);  // Sets the user offline
                            		userDAO.save(user);
                            		
                            	}
                            	
                            }
                        })
                        .permitAll()  
                    )
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            serviceUser.saveOrUpdateUser(oAuth2User);
            if(serviceUser.findByEmail(oAuth2User.getAttribute("email"))!=null) {

            	String username=serviceUser.findByEmail(oAuth2User.getAttribute("email")).getName();
            	String userId=serviceUser.findByEmail(oAuth2User.getAttribute("email")).getId();
            	List<Role> role =serviceUser.findByEmail(oAuth2User.getAttribute("email")).getRoles();
            	User user=serviceUser.findById(userId);
            	user.setOnline(true);
            	userDAO.save(user);
            	request.getSession().setAttribute("name", username);
            	request.getSession().setAttribute("userId", userId);
            	request.getSession().setAttribute("role", role);
            }else {
            	String username = oAuth2User.getAttribute("name");// Puts in session the user name
            	String userId = oAuth2User.getAttribute("userId");
            	String role = oAuth2User.getAttribute("role");
            	User user=serviceUser.findById(userId);
            	user.setOnline(true);
            	userDAO.save(user);
            	request.getSession().setAttribute("name", username);
            	request.getSession().setAttribute("userId", userId);
            	request.getSession().setAttribute("role", role);
            }
            response.sendRedirect("/");
        };
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
        	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        	String email = userDetails.getUsername(); //email
        	Optional<User> optionalUser = userDAO.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String fullName = user.getName();
                String userId = user.getId();
                List<String> role = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                // Set user online
                user.setOnline(true);
                userDAO.save(user);

                // Set attributes in session
                request.getSession().setAttribute("name", fullName);
                request.getSession().setAttribute("userId", userId);
                request.getSession().setAttribute("role", role);  // Set the role in the session
            }
            
            response.sendRedirect("/");
        };
    }

}

