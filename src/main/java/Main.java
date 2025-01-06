import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Main {

  /*
   * 항상 똑같은 순서로 처리됨
   *
   * Reactor 의 기본 동작은
   * 호출한 스레드(main 스레드)에서 동기적으로 데이터 스트림을 처리하는 것입니다.
   *
   * 별도의 비동기 처리(ex: Schedulers)를 명시적으로 추가하지 않았기 때문에, 모든 연산은 순차적으로 실행됩니다.
   */
  public static void main(String[] args) {
    Flux
        .fromArray(new Integer[] {1, 3, 5, 7})
        .doOnNext(data -> System.out.printf("# [%s] doOnNext fromArray: %s\n", Thread.currentThread().getName(), data))
        .publishOn(Schedulers.parallel())
        .filter(data -> data > 3)
        .doOnNext(data -> System.out.printf("# [%s] doOnNext filter: %s\n", Thread.currentThread().getName(), data))
        .publishOn(Schedulers.parallel())
        .map(data -> data * 10)
        .doOnNext(data -> System.out.printf("# [%s] doOnNext map: %s\n", Thread.currentThread().getName(), data))
        .subscribe(data -> System.out.printf("# [%s] onNext: %s\n", Thread.currentThread().getName(), data));
  }
}
