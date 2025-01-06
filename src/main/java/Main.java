import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "company";

    Mono<String> mono = Mono.deferContextual(
            contextView -> Mono.just("Company: " + " " + contextView.get(key1)))
        .publishOn(Schedulers.parallel());

    mono.contextWrite(context -> context.put(key1, "Apple"))
        .subscribe(data -> System.out.printf("# [%s] subscribe1 onNext: %s\n", Thread.currentThread().getName(), data));

    mono.contextWrite(context -> context.put(key1, "Microsoft"))
        .subscribe(data -> System.out.printf("# [%s] subscribe2 onNext: %s\n", Thread.currentThread().getName(), data));



    Thread.sleep(100L);
  }
}
