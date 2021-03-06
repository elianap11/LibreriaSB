package com.libreriaSB.security;

import com.libreriaSB.exceptions.MyException;
import com.libreriaSB.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
//darle permisos al usuario a través del rol
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws MyException, Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                //se establecen rutas disponibles para cualquier usuario
                    .antMatchers("/signup", "/registro", "/CSS/*", "/IMG/*", "/UPLOAD/*").permitAll()
                //todo lo demás debe estar autenticado    
                .antMatchers("/**").authenticated()
                .and()
                //esta es la configuración para el login
                .formLogin()
                    .loginPage("/login")
                //logincheck lo procesa Spring
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("correo")
                        .passwordParameter("clave")
                //el mensaje de éxito envía a la home
                        .defaultSuccessUrl("/index", true)
                //el mensaje de error
                        .failureUrl("/login?error=true")
                        .permitAll()
                .and()
                //el deslogeo
                    .logout()
                //la url la maneja Spring, accedemos a través de un botón
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                //borra las cookis de las sesión
                        .deleteCookies("JSESSIONID")
                .and()
                //previene ataques
                    .csrf()
                    .disable();
        // @formatter:on
    }
}