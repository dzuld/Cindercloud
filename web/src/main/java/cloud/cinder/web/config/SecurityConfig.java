package cloud.cinder.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

        http.formLogin().loginPage("/monitoring/login.html").loginProcessingUrl("/monitoring/login").permitAll();

        http.authorizeRequests()
                .antMatchers("/monitoring/login.html", "/**/*.css", "/monitoring/img/**", "/third-party/**")
                .permitAll();

        http.authorizeRequests().antMatchers("/monitoring/**", "/management/**").fullyAuthenticated();

        http.httpBasic();

        http.logout().logoutUrl("/monitoring/logout");

    }
}
