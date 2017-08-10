package hello;

import hello.token.caching.Authenticator;
import hello.token.caching.Profile;
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
    private final Authenticator authenticator;

    private int i = 0;

    @Override
    public void run(String... args) throws Exception {
        testTokenCaching();
//        testBookService();
//        testBookRepo();
//        testRecovering();
    }

    private void testTokenCaching() {
        final Profile profile = new Profile("name1", "address1");
        for (int k = 0; k < 5; k++) {
            System.out.println("Test function result: " +
                    authenticator.run(profile,
                    token -> {
                        log.info("Token is '{}'", token);
                        i++;
                        if (i == 3) {
                            throw new RecoverNeededException();
                        }
                        return "Success result";
                    }));
        }
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