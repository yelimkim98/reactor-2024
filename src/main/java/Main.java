import java.util.stream.IntStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.scheduler.Schedulers;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    int tasks = 6;

    Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
    Flux<String> fluxView = unicastSink.asFlux();
    IntStream
        .range(1, tasks)
        .forEach(n -> {
          try {
            new Thread(() -> {
              unicastSink.emitNext(doTask(n), EmitFailureHandler.FAIL_FAST);
              System.out.printf("## [%s] emitted: %s\n", Thread.currentThread().getName(), n);
            }).start();
            Thread.sleep(100L);
          } catch (InterruptedException e) {
            System.out.printf("## [%s] %s\n", Thread.currentThread().getName(), e.getMessage());
          }
        });
    fluxView
        .publishOn(Schedulers.parallel())
        .map(result -> result + " success!")
        .doOnNext(
            n -> System.out.printf("## [%s] map(): %s\n", Thread.currentThread().getName(), n))
        .publishOn(Schedulers.parallel())
        .subscribe(
            data -> System.out.printf("## [%s] onNext: %s\n", Thread.currentThread().getName(), data));

    Thread.sleep(200L);
  }

  private static String doTask(int taskNumber) {
    // now tasking
    // complete to task
    return "task " + taskNumber + " result";
  }
}
