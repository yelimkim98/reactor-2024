import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    URI worldTimeUri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("worldtimeapi.org")
        .port(80)
        .path("/api/timezone/Asia/Seoul")
        .build()
        .encode()
        .toUri();

    Mono<String> mono = getWorldTime(worldTimeUri);
    mono.subscribe(datetime -> System.out.printf("# datetime 1: %s%n", datetime));
    Thread.sleep(2000);
    mono.subscribe(datetime -> System.out.printf("# datetime 2: %s%n", datetime));
    Thread.sleep(2000);
  }

  private static Mono<String> getWorldTime(URI worldTimeUri) {
    return WebClient.create()
        .get()
        .uri(worldTimeUri)
        .retrieve()
        .bodyToMono(String.class)
        .map(response -> JsonPath.parse(response).read("$.datetime"));
  }
}
