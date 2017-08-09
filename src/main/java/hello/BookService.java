package hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final CacheManager cacheManager;
    private int counter = 1;
    private boolean wasError = false;

    @Retryable(
            include = RecoverNeededException.class,
            maxAttempts = 1,
            backoff = @Backoff(delay = 100, maxDelay = 101))
    public Book find(String isbn) {
        return findInternal(isbn);
    }

    @Recover //This method is invoked to try recover after all retry fail.
    public Book recoverAfterException(RecoverNeededException e, String isbn) {
        log.info("Recovering after exception. Arg is '{}'. Exception is '{}'", isbn, e.getClass());
        cacheManager.getCache("books").evict(isbn);
        return findInternal(isbn);
    }

    private Book findInternal(String isbn) {
        errorCheck();
        return bookRepository.getByIsbn(isbn);
    }

    private void errorCheck() {
        if (counter % 3 == 0 && !wasError) { //Emulate error to verify retry functionality
            wasError = true;
            throw new RecoverNeededException();
        }
        counter++;
    }
}
