//package com.example.springpoliecobe.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {

    private UserDetailsService userDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder(){        //Da utilizzare nei servizi (tramite @Autowired) per criptare
        return new BCryptPasswordEncoder();                 //la password prima di salvarla nel db.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                //.csrf().and().cors().disable()
                //.exceptionHandling()
                //.and()
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                //.authorizeHttpRequests().requestMatchers("/api/authenticate", "/offerTransactionCall").permitAll()

                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )

                //.authorizeHttpRequests()
                //.requestMatchers("/api/authenticate", "/offerTransactionCall").permitAll()
                //.anyRequest().authenticated()

                /*.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/authenticate", "/offerTransactionCall").permitAll()
                        .anyRequest().authenticated()
                )*/
                /*.formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")       //Tramite form action settato sempre su endpoint
                                .defaultSuccessUrl("/add-user")      //"/login" dovrebbe far girare il Security da qua.
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
                //http.httpBasic();
        return http.build();
    }*/

    /*@Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.requestMatchers("/resource/**").permitAll()
                        .requestMatchers("/api/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers("/db/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                        .anyRequest().denyAll());
        http.httpBasic();
        return http.build();
    }*/


    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests()
                .requestMatchers( "/resources/**", "/", "/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }*/

    /*@Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/offerTransactionCall").permitAll()
                .anyRequest().authenticated();
        }*/


    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}*/