package co.arctern.rider.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String baseUrl;

    @Override
    public void configure(HttpSecurity http) throws Exception {
                http.cors().and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("**"+baseUrl+"/profile").permitAll()
                .antMatchers("**"+baseUrl+"/" ).authenticated();
    }
}
