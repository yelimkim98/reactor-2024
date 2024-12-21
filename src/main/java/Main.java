import reactor.core.publisher.Flux;

public class Main {

  public static void main(String[] args) {
    Flux<String> sequence = Flux.just("Hello", "Reactor");

    sequence.map(String::toLowerCase)
        .subscribe(System.out::println);
  }
}
