package hello;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppRunner implements CommandLineRunner {

    private final RecoverableService recoverableService;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("... Fetching books");
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        testRecovering();
    }

    private void testRecovering() {
        log.info("... Start recovering test");
        execute("foo");
        execute("bar");
    }

    private void execute(final String arg) {
        try {
            recoverableService.execute(arg);
            log.info("Recovered with '{}'", arg);
        } catch (Exception e) {
            log.info("Not recovered with '{}': ", arg, e);
        }
    }


}