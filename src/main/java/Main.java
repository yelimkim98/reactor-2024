import lombok.SneakyThrows;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class Main {

  public static void main(String[] args) {
    Flux.range(1, 5)
        .doOnRequest(data -> System.out.printf("# doOnRequest: %s\n", data))
        .subscribe(new BaseSubscriber<Integer>() {

          /*
           구독 시점에 실행됨
           */
          @Override
          protected void hookOnSubscribe(Subscription subscription) {
            request(1);  // subscription.request(1) 호출
          }

          /*

           */
          @SneakyThrows    // ?
          @Override
          protected void hookOnNext(Integer value) {
            Thread.sleep(2000L);
            System.out.printf("# hookOnNext: %s\n", value);
            request(1);    // // subscription.request(1) 호출
          }
        });
  }
}
