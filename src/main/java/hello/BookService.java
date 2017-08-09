package hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private int counter = 1;
    private boolean wasError = false;

    @Retryable(
            include = RecoverNeededException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100, maxDelay = 101))
    public Book find(String isbn) {
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
