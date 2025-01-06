import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "company";
    String key2 = "name";

    Mono
        .deferContextual(contextView ->
            Mono.just(contextView.get(key1))
        )
        .publishOn(Schedulers.parallel())
        .contextWrite(context -> context.put(key2, "Bill"))
        .transformDeferredContextual((mono, context) ->
            // context 에 key2 가 없다고 됨
            mono.map(data -> data + ", " + context.getOrDefault(key2, "Steve"))
        )
        .contextWrite(context -> context.put(key1, "Apple"))
        .subscribe(data -> System.out.printf("# [%s] onNext: %s\n", Thread.currentThread().getName(), data));

    Thread.sleep(100L);
  }
}
