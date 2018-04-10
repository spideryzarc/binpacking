import java.util.Arrays;
import java.util.Objects;

import static java.util.Arrays.fill;

/**
 * Soluçao do problema de empacotamento
 */
public class Sol implements Comparable<Sol>{

    /**
     * pacote do bin i
     */
    int binOf[];

    /**numero de pacotes usados */
    int count;

    /**desvio padrao*/
    double stdDev;

    /**somo dos pesos dos itens atribuidos a cada pacote */
    int load[];

    /**Atualiza os valores dos atributos count e stdDev*/
    public void updateAtributes(){
        count = 0;

        for (int i = 0; i < bpp.N; i++)
            if(count < binOf[i])
                count = binOf[i];

        load = new int[count+1];
        for (int i = 0; i < bpp.N; i++)
            load[binOf[i]] += bpp.size[i];

        double avg = bpp.sizeSum/count;

        stdDev = 0;
        for (int i = 0; i < count; i++)
            stdDev += (avg-load[i])*(avg-load[i]);


    }

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

    public void copy(Sol s){
        for (int i = 0; i < binOf.length; i++) {
            binOf[i] = s.binOf[i];
        }
        this.count = s.count;
        this.stdDev = s.stdDev;
        this.load = s.load.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sol sol = (Sol) o;
        return Arrays.equals(binOf, sol.binOf) &&
                Objects.equals(bpp, sol.bpp);
    }


    @Override
    public int compareTo(Sol sol) {
        if(this.count != sol.count){
            return Integer.compare(this.count,sol.count);
        }
        return -Double.compare(this.stdDev,sol.stdDev);
    }
}
