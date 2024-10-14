import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class ConversorDeDivisas_ExChangerate {

    public static void main(String[] args) throws IOException {
        ConsultaApi ClienteApi = new ConsultaApi();
        GeneradorJson jsonParserHelper = new GeneradorJson(); // Clase para procesar JSON
        MenuDeOpciones menu = new MenuDeOpciones();

        // Mapa de códigos de moneda con los nombres completos
        Map<String, String> NombresDivisas = new HashMap<>();
        NombresDivisas.put("CAD", "Dolar Canadiense");
        NombresDivisas.put("USD", "Dolar Estadounidense");
        NombresDivisas.put("EUR", "Euro");
        NombresDivisas.put("ARS", "Peso Argentino");
        NombresDivisas.put("BRL", "Real brasileño");
        NombresDivisas.put("MXN", "Peso Mexicano");
        NombresDivisas.put("JPY", "Yen Japones");

        // Lista de códigos de divisas (ordenada según se mostrará en el menú)
        String[] DivisasDisponibles = {"CAD", "USD", "EUR", "ARS", "BRL", "MXN", "JPY"};

        boolean continuar = true;

        try {
            // Obtener las tasas de cambio en USD por defecto
            String jsonResponse = ClienteApi.getExchangeRates("USD");
            JsonObject TasaDeCambio = jsonParserHelper.parseExchangeRates(jsonResponse);
            Map<String, Double> TiposDeCambio = new HashMap<>();
            for (String currency : DivisasDisponibles) {
                if (TasaDeCambio.has(currency)) {
                    TiposDeCambio.put(currency, TasaDeCambio.get(currency).getAsDouble());
                }
            }

            ConversorDeDivisas convertir = new ConversorDeDivisas(TiposDeCambio);
            List<GeneradorJson.Conversion> Historial = new ArrayList<>();

            while (continuar) {
                // Mostrar menú de divisas con nombres completos y códigos
                System.out.println("Selecciona la moneda que tienes:");
                for (int i = 0; i < DivisasDisponibles.length; i++) {
                    String codigoDivisa = DivisasDisponibles[i];
                    System.out.println((i + 1) + ". " + NombresDivisas.get(codigoDivisa) + " \"" + codigoDivisa + "\"");
                }

                // Pedir al usuario que elija la moneda de origen
                int fromCurrencyIndex = menu.ConsultaOpcion("\n Ingresa el número de la moneda que tienes: ", DivisasDisponibles.length);
                String fromCurrency = DivisasDisponibles[fromCurrencyIndex - 1];
                System.out.println("******************************");

                // Mostrar menú de divisas para moneda destino
                System.out.println("Selecciona la moneda a la que deseas convertir:");
                for (int i = 0; i < DivisasDisponibles.length; i++) {
                    String codigoDivisa = DivisasDisponibles[i];
                    System.out.println((i + 1) + ". " + NombresDivisas.get(codigoDivisa) + " \"" + codigoDivisa + "\"");
                }

                int toCurrencyIndex = menu.ConsultaOpcion("Ingresa el número de la moneda a la que deseas convertir: ", DivisasDisponibles.length);
                String toCurrency = DivisasDisponibles[toCurrencyIndex - 1];
                System.out.println("-----------------");

                // Verificar que no se está intentando convertir la misma moneda
                if (fromCurrency.equals(toCurrency)) {
                    System.out.println("No puedes convertir una moneda a sí misma.");
                    continue;
                }

                // Pedir la cantidad a convertir
                double amount = menu.ConsultaMonto("Ingresa la cantidad que deseas convertir: ");

                // Realizar la conversión si las monedas son válidas
                if (TiposDeCambio.containsKey(fromCurrency) && TiposDeCambio.containsKey(toCurrency)) {
                    double resultado = convertir.convert(fromCurrency, toCurrency, amount);
                    menu.MostrarConversion(resultado, fromCurrency, toCurrency, amount);

                    // Crear un objeto Conversion y agregar al historial
                    jsonParserHelper.agregarConversion(fromCurrency, toCurrency, amount, resultado);
                    Historial.add(new GeneradorJson.Conversion(fromCurrency, toCurrency, amount, resultado));

                } else {
                    System.out.println("Moneda no soportada.");
                }

                // Preguntar si desea continuar
                continuar = menu.ConsultaParaContinuar();
            }

            System.out.println("\n*************************************************");
            System.out.println("Gracias por usar el convertidor de monedas.");
            System.out.println("*************************************************");
            if (!Historial.isEmpty()) {
                System.out.println("\n*********************************");
                System.out.println("Historial de conversiones: ");
                System.out.println("*********************************");
                for (GeneradorJson.Conversion conversion : Historial) {
                    System.out.println("De " + conversion.fromCurrency + " a " + conversion.toCurrency + ": " + conversion.amount + " = " + conversion.result + " (" + conversion.timestamp + ")");
                }
            }

            // Generar el archivo JSON con el historial completo
            jsonParserHelper.generarJson();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}