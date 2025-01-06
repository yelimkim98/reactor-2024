import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "company";
    String key2 = "firstname";
    String key3 = "lastname";

    Mono.deferContextual(ctx ->
            Mono.just(ctx.get(key1) + ", " + ctx.get(key2) + ", " + ctx.get(key3))
        )
        .publishOn(Schedulers.parallel())
        .contextWrite(context ->
            context.putAll(Context.of(key2, "Steve", key3, "Jobs").readOnly())
        )
        .contextWrite(context -> context.put(key1, "Apple"))
        .subscribe(data -> System.out.printf("# [%s] onNext: %s\n", Thread.currentThread().getName(), data));

    Thread.sleep(100L);
  }
}
