import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {

        try(var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(2000))
                .build()) {

            HttpRequest httpRequest1 = HttpRequest.newBuilder(URI.create("https://api.mockae.com/fakeapi/products/1"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();



            HttpRequest httpRequest2 = HttpRequest.newBuilder(URI.create("https://api.mockae.com/fakeapi/products/2"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();


            CompletableFuture<HttpResponse<String>> sentAsync1 = httpClient.sendAsync(httpRequest1, HttpResponse.BodyHandlers.ofString());
            CompletableFuture<HttpResponse<String>> sentAsync2 = httpClient.sendAsync(httpRequest2, HttpResponse.BodyHandlers.ofString());

            sentAsync1.thenAccept(stringHttpResponse -> {
                System.out.println("Response 1 status: " + stringHttpResponse.statusCode());
                System.out.println("Response 1 body: " + stringHttpResponse.body());
            });


            sentAsync2.thenAccept(stringHttpResponse -> {
                System.out.println("Response 2 status: " + stringHttpResponse.statusCode());
                System.out.println("Response 2 body: " + stringHttpResponse.body());
            });


            CompletableFuture.allOf(sentAsync1, sentAsync2)
                    .join();




        }
    }
}
