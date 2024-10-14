import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GeneradorJson {
    private List<Conversion> conversiones = new ArrayList<>(); // Lista para almacenar conversiones

    public static class Conversion {
        public String fromCurrency;
        public String toCurrency;
        public double amount;
        public double result;
        public String timestamp;

        public Conversion(String fromCurrency, String toCurrency, double amount, double result) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.amount = amount;
            this.result = result;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public void agregarConversion(String fromCurrency, String toCurrency, double amount, double result) {
        Conversion conversion = new Conversion(fromCurrency, toCurrency, amount, result);
        conversiones.add(conversion); // Agrega la conversión a la lista
    }

    public void generarJson() throws IOException {
        // Crea un objeto Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Convierte la lista de conversiones a JSON
        String json = gson.toJson(conversiones);

        // Escribe el JSON en un archivo
        try (FileWriter writer = new FileWriter("conversiones.json")) {
            writer.write(json);
        }
    }

    public JsonObject parseExchangeRates(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        return jsonObject.getAsJsonObject("conversion_rates");
    }
}