package it.airdesk.airdesk_app.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

        @Autowired
        private DataSource dataSource;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.jdbcAuthentication()
                                .dataSource(dataSource)
                                .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
                                .usersByUsernameQuery(
                                                "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
                return new OAuth2AuthenticationSuccessHandler(); // Register your custom success handler
        }

        //@SuppressWarnings({ "removal", "deprecation" })
        @Bean
        protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(csrf -> csrf.disable()) // Disabilita CSRF se necessario, o configuralo come
                                                              // richiesto
                                .cors(cors -> cors.disable()) // Disabilita CORS se necessario, o configuralo come
                                                              // richiesto
                                // AUTORIZZAZIONE: here we define who can access what
                                .authorizeHttpRequests(authorize -> authorize
                                                // chiunque (autenticato o no) può accedere alle pagine index, login,
                                                // register,
                                                // ai css e alle immagini
                                                .requestMatchers(HttpMethod.GET, "/", "/oauth2/**", "/index", "/register", "/css/**",
                                                                "/images/**", "/searchFacilities", "/bookingMenu/**",
                                                                "favicon.ico", "/error")
                                                .permitAll()
                                                // chiunque (autenticato o no) può mandare richieste POST al punto di
                                                // accesso
                                                // per login e register
                                                .requestMatchers(HttpMethod.POST, "/register", "/login","/bookingMenu/**", "/bookWorkstation")
                                                .permitAll()
                                                // solo gli utenti autenticati con ruolo ADMIN possono accedere a
                                                // risorse
                                                // con
                                                // path /formNewStruttura
                                                .requestMatchers(HttpMethod.GET, "/admin/**")
                                                .hasAnyAuthority("USER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/admin/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                // tutti gli utenti autenticati possono accere alle pag
                                                .anyRequest().authenticated())
                                                

                                // LOGIN: here we define how the authentication gets handled
                                // usiamo il protocollo formlogin
                                .formLogin(formLogin -> formLogin
                                                // the login page is /login
                                                .loginPage("/login")
                                                .permitAll()
                                                // se il login ha successo, si viene rediretti al path /default
                                                .defaultSuccessUrl("/success", true)
                                                .failureUrl("/login?error=true"))
                                
                                //OAUTH: here we define the OAuth2.0 login
                                .oauth2Login(oauth2Login -> oauth2Login
                                                .loginPage("/login")
                                                .successHandler(oAuth2AuthenticationSuccessHandler()))

                                // LOGOUT: here we define the logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .clearAuthentication(true).permitAll());

                return httpSecurity.build();
        }
}