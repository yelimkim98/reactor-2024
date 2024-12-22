import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Flux.interval(Duration.ofMillis(1L))    // Publisher : 0.001 초에 한번씩 emit (계속 반복)
        .onBackpressureError()    // Backpressure 전략 중 ERROR 전략 사용
        .doOnNext(data -> System.out.printf("# doOnNext: %s\n", data))  // 걍 디버깅용 이라고 함
        .publishOn(Schedulers.parallel())    // ?
        .subscribe(data -> {
              try {
                Thread.sleep(5L);    // Subscriber 가 데이터 처리하는데 0.005 초 걸리는 시나리오
              } catch (InterruptedException e) {
              }

              System.out.printf("# onNext : %s\n", data);
            },
            error -> System.out.println("# onError"));

    Thread.sleep(2000L);
  }
}
