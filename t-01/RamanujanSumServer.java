import java.math.BigDecimal;
import java.math.MathContext;
import java.io.*;
import java.net.*;

public class RamanujanSumServer {
    public static void main(String[] args) {
        int port = 3000; // Puerto en el que el servidor escuchará las conexiones

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor esperando conexiones en el puerto " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Espera a que llegue una conexión

                // Manejar la conexión en un nuevo hilo
                new Thread(new RamanujanSumServerWorker(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Serverworker

class RamanujanSumServerWorker implements Runnable {
    private Socket clientSocket;

    public RamanujanSumServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static BigDecimal calcularSumaRamanujan(int inicio, int fin) {
        MathContext mc = new MathContext(100);
        BigDecimal sum = BigDecimal.ZERO;

        for (int k = inicio; k < fin; k++) {
            BigDecimal numerator = factorial(4 * k)
                    .multiply(BigDecimal.valueOf(1103).add(BigDecimal.valueOf(26390 * k)));
            BigDecimal denominator = factorial(k).pow(4).multiply(BigDecimal.valueOf(396).pow(4 * k));
            sum = sum.add(numerator.divide(denominator, mc));
        }

        return sum;
    }

    public static BigDecimal factorial(int n) {
        BigDecimal fact = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            fact = fact.multiply(BigDecimal.valueOf(i));
        }
        return fact;
    }

    @Override
    public void run() {
        try {
            // Procesar solicitud del cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = in.readLine();
            System.out.println("Solicitud recibida del cliente: " + request);

            String[] partes = request.split(",");
            System.out.println("Suma parcial desde k = " + partes[0]);
            System.out.println("hasta k = " + partes[1]);

            int kInicial = Integer.valueOf(partes[0]);
            int kFinal = Integer.valueOf(partes[1]);

            BigDecimal parteSuma = calcularSumaRamanujan(kInicial, kFinal);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(parteSuma);

            // Cerrar la conexión
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}