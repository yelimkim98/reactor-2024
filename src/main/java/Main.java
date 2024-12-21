import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    String[] singers = {"A", "B", "C", "D", "E"};

    System.out.println("# begin concert:");

    Flux<String> concertFlux =
        Flux
            .fromArray(singers)
            .delayElements(Duration.ofSeconds(1))
            .share();

    concertFlux.subscribe(
        singer -> System.out.println(String.format("# Subscriber1 is watching %s's song", singer))
    );
    Thread.sleep(2500);

    concertFlux.subscribe(
        singer -> System.out.println(String.format("# Subscriber2 is watching %s's song", singer))
    );
    Thread.sleep(3000);
  }
}
