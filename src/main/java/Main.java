import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Sinks.One<String> sinkOne = Sinks.one();
    Mono<String> mono = sinkOne.asMono();

    sinkOne.emitValue("Hello Reactor", EmitFailureHandler.FAIL_FAST);
    sinkOne.emitValue("Hi Reactor", EmitFailureHandler.FAIL_FAST);

    mono.subscribe(data -> System.out.printf("## [%s] Subscriber1 %s\n", Thread.currentThread().getName(), data));
    mono.subscribe(data -> System.out.printf("## [%s] Subscriber2 %s\n", Thread.currentThread().getName(), data));


  }
}
