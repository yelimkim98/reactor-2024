import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {

  public static void main(String[] args) {
    Mono.empty()
        .subscribe(
            none -> {},
            error -> {},
            () -> System.out.println("onComplete")
        );
  }
}
