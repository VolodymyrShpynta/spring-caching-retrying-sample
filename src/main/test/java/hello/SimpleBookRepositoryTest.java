package hello;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by vshpynta on 22.08.17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SimpleBookRepositoryTest.TestConfig.class})
public class SimpleBookRepositoryTest {

    private static final String CACHE_NAME = "books";
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private BookRepository bookRepository;

    @After
    public void tearDown() throws Exception {
        cacheManager.getCache(CACHE_NAME).clear();
    }

    @Test
    public void testGetByIsbn() {
        final String isbn = "isbn-1234";
        final Book book1 = bookRepository.getByIsbn(isbn);
        final Book book2 = bookRepository.getByIsbn(isbn);
        assertTrue(book1 == book2); //book2 should be taken from cache
    }


    @Configuration
    @EnableCaching
    @ComponentScan("hello")
    public static class TestConfig {
        @Bean
        public SimpleCacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            List<Cache> caches = new ArrayList<>();
            caches.add(cacheBean().getObject());
            cacheManager.setCaches(caches);
            return cacheManager;
        }

        @Bean
        public ConcurrentMapCacheFactoryBean cacheBean() {
            ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
            cacheFactoryBean.setName(CACHE_NAME);
            return cacheFactoryBean;
        }
    }
}