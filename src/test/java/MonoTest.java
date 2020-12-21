import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

class MonoTest {

    @Test
    void monoTest() {
        Mono.just("A")
                .log()
                .map(x -> x.toLowerCase())
                .subscribe();
    }

    @Test
    void fluxTest() throws InterruptedException {
        Flux.range(2, 5)
                .log()
                .subscribe();
    }

    @Test
    void unitTest() throws InterruptedException {
        new Unit<String>()
                .log()
                .just("Hello World")
                .map(String::toLowerCase)
                .subscribe();

        Mono.just("A")
                .log()
                .map(x -> x.toLowerCase())
                .subscribe();

        Thread.sleep(1000);
    }

    @Test
    @SuppressWarnings("unchecked")
    void oneShotPublisherTest() throws InterruptedException {
        DomainSubscriber<String> tony = new DomainSubscriber<>("Hello");

        DomainPublisher<String> mangaFox = new DomainPublisher<>(true);
        DomainPublisher<List<String>> madHouse = new DomainPublisher<>(true);

        mangaFox.subscribe(tony);

        Thread.sleep(1000);

    }
}
