package cloud.cinder.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http.cors();

        http.formLogin().loginPage("/monitoring/login.html").loginProcessingUrl("/monitoring/login").permitAll();

        http.authorizeRequests()
                .antMatchers("/monitoring/login.html", "/**/*.css", "/monitoring/img/**", "/third-party/**")
                .permitAll();

        http.authorizeRequests().antMatchers("/monitoring/**", "/management/**").fullyAuthenticated();

        http.httpBasic();

        http.logout().logoutUrl("/monitoring/logout");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://wallet.cinder.cloud", "https://cinder.cloud", "http://localhost:8080", "http://localhost:9090"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
