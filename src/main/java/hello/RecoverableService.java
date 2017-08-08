package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecoverableService {
    @Retryable(
            include = RecoverNeededException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, maxDelay = 101))
    public void execute(String arg) {
        log.info("Executing with arg '{}'", arg);
        if ("foo".equals(arg)) {
            throw new RecoverNeededException();
        } else {
            throw new IllegalStateException("Is not recoverable");
        }
    }

    @Recover
    public void recoverNeededException(RecoverNeededException e, String arg) {
        log.info("Recovered from exception. Arg is '{}'. Exception is '{}'", arg, e.getClass());
    }

    @Recover
    public void otherException(Exception e, String arg) throws Exception {
        log.info("Recovering failure. Arg is '{}'. Exception is '{}'", arg, e.getClass());
        throw e;
    }
}
