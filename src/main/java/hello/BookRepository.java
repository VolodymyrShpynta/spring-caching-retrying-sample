package hello;

import org.springframework.cache.annotation.Cacheable;

public interface BookRepository {
    
    @Cacheable("books")
    Book getByIsbn(String isbn);
}