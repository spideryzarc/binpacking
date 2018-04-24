import java.util.Arrays;
import java.util.Objects;

import static java.util.Arrays.fill;

/**
 * Soluçao do problema de empacotamento
 */
public class Sol implements Comparable<Sol> {

    /**
     * pacote do bin i
     */
    int binOf[];

    /**
     * numero de pacotes usados
     */
    int binCount;

    /**
     * desvio padrao
     */
    double stdDev;

    /**
     * soma dos pesos dos itens atribuidos a cada pacote
     */
    int load[];

    /**
     * Atualiza os valores dos atributos binCount e stdDev
     */
    public void updateAtributes() {
        binCount = binCount();

        if (load == null || load.length != binCount)
            load = new int[binCount];
        else
            fill(load, 0);

        for (int i = 0; i < bpp.N; i++)
            load[binOf[i]] += bpp.size[i];

        double avg = bpp.sizeSum / binCount;

        stdDev = 0;
        for (int j = 0; j < binCount; j++) {
            double x = avg - load[j];
            stdDev += x * x;
        }

    }

    /**
     * referencia para o problema de empacotamento
     */
    protected BPP bpp;

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
        return "Sol{" +
                "binCount=" + binCount +
                ", stdDev=" + stdDev +
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
                for (int j = bpp.N - 1; j > i; j--) {
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

    public int bestFitRandom(int idx[]) {
        fill(binOf, -1);
        int binCount = 0;
        for (int a = 0; a < bpp.N; a++) {
            int i = idx[a];
            if (binOf[i] == -1) {
                int residuo = bpp.C - bpp.size[i];
                binOf[i] = binCount;
                for (int b = a + 1; b < bpp.N; b++) {
                    int j = idx[b];
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


    public void copy(Sol s) {
        for (int i = 0; i < binOf.length; i++)
            binOf[i] = s.binOf[i];

        this.binCount = s.binCount;
        this.stdDev = s.stdDev;

        if (s.load != null) {
            if (load == null || load.length != s.load.length)
                this.load = s.load.clone();
            else
                for (int i = 0; i < s.load.length; i++)
                    load[i] = s.load[i];
        }

    }

    /**
     * @return soma das violações de capacidade
     */
    public int violation() {
        int v = 0;
        for (int l : load) {
            if (l > bpp.C)
                v += l-bpp.C;
        }
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sol sol = (Sol) o;
        return Arrays.equals(binOf, sol.binOf) &&
                Objects.equals(bpp, sol.bpp);
    }

    /**
     * @return negativo se esta solução for melhor do que o parametro 'sol', positivo se pior e zero se igual
     */
    @Override
    public int compareTo(Sol sol) {
        if (this.binCount != sol.binCount) {
            return Integer.compare(this.binCount, sol.binCount);
        }
        return -Double.compare(this.stdDev, sol.stdDev);
    }

    public int dist(Sol sol) {
        int c = 0;
        for (int i = 0; i < bpp.N; i++)
            if (binOf[i] != sol.binOf[i])
                c++;
        return c;
    }
}
