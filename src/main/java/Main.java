import java.util.stream.IntStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    int tasks = 6;

    Flux.create((FluxSink<String> sink) -> IntStream.range(1, tasks)
        .forEach(n -> sink.next("task " + n + " result")))
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext(n -> System.out.printf("### [%s] create(): %s\n", Thread.currentThread().getName(),n))
        .publishOn(Schedulers.parallel())
        .map(result -> result + " success!")
        .doOnNext(n -> System.out.printf("### [%s] map(): %s\n", Thread.currentThread().getName(),n))
        .publishOn(Schedulers.parallel())
        .subscribe(data -> System.out.printf("### [%s] onNext: %s\n", Thread.currentThread().getName(), data));

    Thread.sleep(500L);
  }
}
