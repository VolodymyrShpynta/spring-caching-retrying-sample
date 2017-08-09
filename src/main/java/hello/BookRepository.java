package hello;

import org.springframework.cache.annotation.Cacheable;

public interface BookRepository {

    @Cacheable(cacheNames = "books", key = "#isbn")
    Book getByIsbn(String isbn);
}