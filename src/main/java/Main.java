import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.util.Collections;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {

  public static void main(String[] args) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("worldtimeapi.org")
        .port(80)
        .path("api/timezone/Asia/Seoul")
        .build()
        .encode()
        .toUri();

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Mono.just(restTemplate.exchange(
            uri, HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class
        ))
        .map(response -> {
          DocumentContext jsonContext = JsonPath.parse(response.getBody());
          return jsonContext.<String>read("$.datetime");
        })
        .subscribe(
            data -> System.out.println("# emitted data: " + data),
            System.out::println,
            () -> System.out.println("# emitted onComplete signal")
        );
  }
}
