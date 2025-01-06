import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Main {

  private static final String HEADER_AUTH_TOKEN = "authToken";

  public static void main(String[] args) {
    Mono<String> mono = postBook(Mono.just(new Book("abdc-1111-3533-2809", "Reactor's Bible", "Kevin")))
        .contextWrite(Context.of(HEADER_AUTH_TOKEN, "eyJhbGciOi"));

    mono.subscribe(data -> System.out.printf("# [%s] onNext: %s", Thread.currentThread().getName(), data));
  }

  private static Mono<String> postBook(Mono<Book> book) {
    return Mono.zip(
            book,
            Mono.deferContextual(contextView -> Mono.just(contextView.get(HEADER_AUTH_TOKEN)))
        )
        .flatMap(tuple -> {
          String response =
              "POST the book(" + tuple.getT1().getBookName() + "," + tuple.getT1().getAuthor()
                  + ") with token: " + tuple.getT2();
          return Mono.just(response);  // HTTP POST 전송을 했다고 가정
        });
  }
}

@AllArgsConstructor
@Data
class Book {

  private String isbn;
  private String bookName;
  private String author;
}