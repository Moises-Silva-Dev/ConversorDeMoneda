import java.text.DecimalFormat;
import java.util.Scanner;

public class MenuDeOpciones {

    private Scanner scanner = new Scanner(System.in);

    // Consulta la opción de un menú, asegurándose de que esté dentro del rango válido
    public int ConsultaOpcion(String message, int maxOption) {
        int opcion = -1;
        while (opcion < 1 || opcion > maxOption) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                if (opcion < 1 || opcion > maxOption) {
                    System.out.println("\n -----------------------------------------------------------");
                    System.out.println("Por favor, ingresa una opción entre 1 y " + maxOption);
                    System.out.println("-----------------------------------------------------------");
                }
            } else {
                System.out.println("\n -----------------------------------------------------------");
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.next();  // Descartar entrada no válida
                System.out.println("-----------------------------------------------------------");
            }
        }
        return opcion;
    }

    // Consulta el monto a convertir asegurándose de que sea un número válido
    public double ConsultaMonto(String message) {
        double monto = -1;
        while (monto < 0) {
            System.out.print(message);
            if (scanner.hasNextDouble()) {
                monto = scanner.nextDouble();
            } else {
                System.out.println("\n -----------------------------------------------------------");
                System.out.println("Entrada no válida. Por favor, ingresa un monto numérico.");
                scanner.next();  // Descartar entrada no válida
                System.out.println("-----------------------------------------------------------");
            }
        }
        return monto;
    }

    // Muestra el resultado de la conversión
    public void MostrarConversion(double resultado, String fromCurrency, String toCurrency, double amount) {
        DecimalFormat df=new DecimalFormat("#.00");
        System.out.println("-----------------");
        System.out.println("Conversión realizada:");
        System.out.printf("%.2f %s equivalen a %s %s%n", amount, fromCurrency, df.format(resultado), toCurrency);
        System.out.println("-----------------");
    }

    // Consulta si el usuario desea realizar otra conversión
    public boolean ConsultaParaContinuar() {
        System.out.println("\n*******************************************");
        System.out.print("¿Deseas realizar otra conversión? (S/N): ");
        String response = scanner.next();
        System.out.println("*******************************************");
        return response.equalsIgnoreCase("S");
    }
}