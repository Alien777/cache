package pl.lasota.cache;

import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

public class ExpiredCacheProcessorTest {

    @Test
    public void generalTestExpiredCache() throws InterruptedException {
        ExpiredCache<Integer, String> cache = new ExpiredCache<>(2000, TimeUnit.MILLISECONDS);
        cache.add(1, "a");
        cache.add(2, "b");
        cache.add(3, "c");
        cache.add(4, "d");
        cache.add(5, "e");

        assert cache.size() == 5;
        assert cache.get(2).orElse("test").equals("b");

        Thread.sleep(2000);
        assert cache.get(1).isEmpty();

        cache.add(6, "d");
        cache.add(7, "e");

        Thread.sleep(1500);

        cache.add(8, "k");

        Thread.sleep(1000);

        assert cache.get(8).orElse("test").equals("k");
        assert cache.size() == 7;

        Thread.sleep(2000);

        cache.add(9, "b");
        cache.add(10, "b");

        cache.cleanup();

        assert cache.size() == 2;

    }

    @Test
    public void differentExpiredCacheTest() throws InterruptedException {
        ExpiredCache<Integer, String> cache = new ExpiredCache<>(1500, TimeUnit.MILLISECONDS);
        CacheControl.instance().addCache("test", cache);

        cache.add(1, "a", 1000);
        cache.add(2, "b");
        cache.add(3, "c", 3000);

        assert cache.size() == 3;
        Thread.sleep(2000);
        assert cache.get(1).isEmpty();
        assert cache.size() == 2;
        assert cache.get(3).orElse("test").equals("c");
        assert cache.get(2).isEmpty();
    }

}
