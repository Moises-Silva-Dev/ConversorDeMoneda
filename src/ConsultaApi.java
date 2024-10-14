import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaApi {

    private static final String API_KEY = "268461ec2b07fc13c67f16a9"; // Reemplazar con la API key real
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public String getExchangeRates(String baseCurrency) throws Exception {
        String url = BASE_URL + baseCurrency;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Error al realizar la solicitud HTTP: " + response.statusCode());
        }
    }
}