import java.util.Map;

public class ConversorDeDivisas {

    private Map<String, Double> exchangeRates;

    public ConversorDeDivisas(Map<String, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public double convert(String fromCurrency, String toCurrency, double amount) {
        double fromRate = exchangeRates.getOrDefault(fromCurrency, 1.0);
        double toRate = exchangeRates.getOrDefault(toCurrency, 1.0);
        return amount * (toRate / fromRate);
    }
}