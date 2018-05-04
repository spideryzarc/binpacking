import java.util.Random;

public class Utils {
    public static final double eps = .01;
    public static Random rd = new Random(7);

    public static void shuffler(int v[]) {
        for (int i = v.length - 1; i > 1; i--) {
            int x = rd.nextInt(i);
            int aux = v[i];
            v[i] = v[x];
            v[x] = aux;
        }
    }

    /**
     * função de avaliação
     */
    public static double stdDev(int[] load, double avg) {
        double soma = 0;
        for (int i = 0; i < load.length; i++) {
            double x = (load[i] - avg);
            soma += x * x;
        }
        return soma;
    }

    public static int roleta(double[] p) {
        for (int i = 1; i < p.length; i++)
            p[i] += p[i - 1];
        if (p[p.length - 1] == 0)
            return -1;
        double x = rd.nextDouble() * p[p.length - 1];
        for (int i = 0; i < p.length; i++)
            if (p[i] > x)
                return i;

        return -1;
    }
}
