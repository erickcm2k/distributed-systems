public class Main {
    public static void main(String[] args) {
        int n = 30; // Número que deseas dividir en intervalos
        int[] intervalos = dividirEnIntervalos(n, 3);
        for (int i = 0; i < intervalos.length - 1; i++) {
            // System.out.println("Intervalo " + (i + 1) + ": [" + intervalos[i] + ", " +
            // intervalos[i + 1] + "]");

        }

        int limInf1 = intervalos[0];
        int limSup1 = intervalos[1];
        int limInf2 = (intervalos[1] + 1);
        int limSup2 = intervalos[2];
        int limInf3 = (intervalos[2] + 1);
        int limSup3 = intervalos[3];

        System.out.println("Intervalo 0 = " + ": [" + limInf1 + ", " + limSup1 + "]");
        System.out.println("Intervalo 1 = " + ": [" + limInf2 + ", " + limSup2 + "]");
        System.out.println("Intervalo 2 = " + ": [" + limInf3 + ", " + limSup3 + "]");
    }

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
}
