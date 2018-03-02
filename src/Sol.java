import java.util.Arrays;

public class Sol {
    int binOf[];
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
        return "Sol{" +
                "binOf=" + Arrays.toString(binOf) +
                '}';
    }
}
