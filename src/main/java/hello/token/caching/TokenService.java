package hello.token.caching;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

import java.util.function.Function;

@Data
@Slf4j
public class TokenService {

    public static final String TOKENS_CACHE_NAME = "auth_tokens_cache";

    private final Function<Profile, String> tokenFunction;

    @Cacheable(cacheNames = TOKENS_CACHE_NAME, key = "#profile.name")
    public String getToken(Profile profile) {
        log.info("Start retrieving token for profile '{}'", profile);
        return tokenFunction.apply(profile);
    }
}
