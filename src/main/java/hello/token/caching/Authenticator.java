package hello.token.caching;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.util.function.Function;

import static hello.token.caching.TokenService.TOKENS_CACHE_NAME;

@Data
@Slf4j
public class Authenticator {

    private final CacheManager cacheManager;
    private final TokenService tokenService;
    private final Function<Exception, Boolean> recoveryDecisionMaker;

    @Retryable(
            include = Exception.class,
            maxAttempts = 1,
            backoff = @Backoff(delay = 100, maxDelay = 101))
    public <T> T run(Profile profile, Function<String, T> function) {
        return function.apply(tokenService.getToken(profile));
    }

    @Recover //This method is invoked to try recover after all retry fail.
    public <T> T recoverAfterException(Exception e, Profile profile, Function<String, T> function) {
        log.info("Start recovering...");
        if (recoveryDecisionMaker.apply(e)) {
            cacheManager.getCache(TOKENS_CACHE_NAME).evict(createKey(profile));
            return function.apply(tokenService.getToken(profile));
        }
        throw new RuntimeException(e);
    }

    private String createKey(Profile profile) {
        return profile.getName();
    }
}
