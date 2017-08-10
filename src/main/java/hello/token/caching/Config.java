package hello.token.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public TokenService tokenService() {
        return new TokenService(profile -> "TOKEN-" + profile.getName());
    }

    @Bean
    public Authenticator authenticator() {
        return new Authenticator(cacheManager, tokenService(), e -> true);
    }
}
