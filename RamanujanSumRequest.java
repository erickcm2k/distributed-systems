import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.*;
import java.net.*;

public class RamanujanSumRequest {

    public static int[] dividirEnIntervalos(int n, int numIntervalos) {
        int[] intervalos = new int[numIntervalos + 1];
        int intervaloSize = n / numIntervalos;
        int resto = n % numIntervalos;
        intervalos[0] = 0;
        for (int i = 1; i <= numIntervalos; i++) {
            intervalos[i] = intervalos[i - 1] + intervaloSize;
            if (resto > 0) {
                intervalos[i]++;
                resto--;
            }
        }
        intervalos[numIntervalos] = n; // Asegurarse de que el último intervalo termine en n
        return intervalos;
    }

    public static BigDecimal RamanujanSumReq(int kInicial, int Kfinal, int idK) {

        String serverAddress = ""; // Dirección del servidor
        int serverPort = 8080; // Puerto del servidor
        BigDecimal BDResponse = new BigDecimal("0");

        // T1-2019630420-1
        String ipVM1 = "172.172.161.200";

        // T1-2019630420-2
        String ipVM2 = "20.162.112.76";

        // T1-2019630420-3
        String ipVM3 = "20.38.39.213";

        if (idK == 0) {
            serverAddress = ipVM1;
        } else if (idK == 1) {
            serverAddress = ipVM2;
        } else {
            serverAddress = ipVM3;
        }

        try (Socket socket = new Socket(serverAddress, serverPort)) {

            // // Establecer conexión con el servidor
            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            // Enviar solicitud al servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.write(kInicial + "," + Kfinal + ","); // Primer parámetro
            out.flush();
            out.println("CALCULATE_PI");

            // Recibir respuesta del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();

            BDResponse = new BigDecimal(response);
            return BDResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return BDResponse;
        }

    }

    public static void main(String[] args) {
        BigDecimal piEstimate = calcularPi(0, 100);
        System.out.println("Estimación de Pi usando la suma de Ramanujan: " + piEstimate);

    }

    public static BigDecimal executeProgram(int kiRequest, int kfRequest) {
        BigDecimal piEstimate = calcularPi(kiRequest, kfRequest);
        System.out.println("Estimación de Pi usando la suma de Ramanujan: " + piEstimate);
        return piEstimate;
    }

    public static BigDecimal calcularPi(int ki, int kf) {
        int numThreads = 3;
        MathContext mc = new MathContext(10000);
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal constant = new BigDecimal("2.0").multiply(sqrt(BigDecimal.valueOf(2)))
                .divide(BigDecimal.valueOf(9801), mc);
        int numTerms = kf;

        int n = kf; // Número que deseas dividir en intervalos
        int[] intervalos = dividirEnIntervalos(n, 3);

        int limInf1 = intervalos[0];
        int limSup1 = intervalos[1];
        int limInf2 = (intervalos[1] + 1);
        int limSup2 = intervalos[2];
        int limInf3 = (intervalos[2] + 1);
        int limSup3 = intervalos[3];

        System.out.println("Intervalo 0 = " + ": [" + limInf1 + ", " + limSup1 + "]");
        System.out.println("Intervalo 1 = " + ": [" + limInf2 + ", " + limSup2 + "]");
        System.out.println("Intervalo 2 = " + ": [" + limInf3 + ", " + limSup3 + "]");

        try {
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Callable<BigDecimal>> tasks = new ArrayList<>();

            tasks.add(() -> RamanujanSumReq(limInf1, limSup1, 0));
            tasks.add(() -> RamanujanSumReq(limInf2, limSup2, 1));
            tasks.add(() -> RamanujanSumReq(limInf3, limSup3, 2));

            List<Future<BigDecimal>> results = executor.invokeAll(tasks);

            System.out.println("El numero de tareas es: " + results.size());

            for (Future<BigDecimal> result : results) {
                System.out.println("Resultado = " + result.get());
                System.out.println();
                sum = sum.add(result.get());
            }

            executor.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return BigDecimal.ONE.divide(sum.multiply(constant), mc);
    }

    public static BigDecimal factorial(int n) {
        BigDecimal fact = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            fact = fact.multiply(BigDecimal.valueOf(i));
        }
        return fact;
    }

    public static BigDecimal sqrt(BigDecimal x) {
        BigDecimal sqrt = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
        return sqrt;
    }
}
