package hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppRunner implements CommandLineRunner {

    private final RecoverableService recoverableService;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Override
    public void run(String... args) throws Exception {
        testBookService();
//        testBookRepo();
//        testRecovering();
    }

    private void testBookService() {
        log.info("... Start BookService test");
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
        log.info("isbn-1234 -->" + bookService.find("isbn-1234"));
    }

    private void testBookRepo() {
        log.info("... Fetching books");
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        log.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
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