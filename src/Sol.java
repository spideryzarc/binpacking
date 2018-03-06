import java.util.Arrays;

import static java.util.Arrays.fill;

/**
 * Soluçao do problema de empacotamento
 */
public class Sol {
    /**
     * pacoto do bin i
     */
    int binOf[];
    /**
     * referencia para o problema de empacotamento
     */
    private BPP bpp;

    public Sol(BPP bpp) {
        this.bpp = bpp;
        binOf = new int[bpp.N];
    }

    /**
     * quantidade de pacotes usados
     */
    public int binCount() {
        int m = 0;
        for (int x : binOf)
            if (m < x)
                m = x;
        return m + 1;
    }

    /**
     * soma dos pesos dos itens no pacote b
     */
    public int load(int b) {
        int load = 0;
        for (int i = 0; i < binOf.length; i++)
            if (binOf[i] == b)
                load += bpp.size[i];

        return load;
    }

    @Override
    public String toString() {
        return "Sol{ bins: " + binCount()+", "+
                "binOf=" + Arrays.toString(binOf) +
                '}';
    }

    public int bestFit() {
        fill(binOf, -1);
        int binCount = 0;

        for (int i = 0; i < bpp.N; i++) {
            if (binOf[i] == -1) {
                int residuo = bpp.C - bpp.size[i];
                binOf[i] = binCount;
                for (int j = i + 1; j < bpp.N; j++) {
                    if (binOf[j] == -1 && bpp.size[j] <= residuo) {
                        binOf[j] = binCount;
                        residuo -= bpp.size[j];
                    }
                }
                binCount++;
            }
        }
        return binCount;
    }

    public int worstFit() {
        fill(binOf, -1);
        int binCount = 0;

        for (int i = 0; i < bpp.N; i++) {
            if (binOf[i] == -1) {
                int residuo = bpp.C - bpp.size[i];
                binOf[i] = binCount;
                for (int j = bpp.N-1; j > i; j--) {
                    if (binOf[j] == -1 && bpp.size[j] <= residuo) {
                        binOf[j] = binCount;
                        residuo -= bpp.size[j];
                    }
                }
                binCount++;
            }
        }
        return binCount;
    }

}
