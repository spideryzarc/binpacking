import java.util.Arrays;

public class BPP {
    /**tamanho de cada item*/
    int size[];
    /**quantidade de itens*/
    int N;
    /**capacidade dos pacotes*/
    int C;

    @Override
    public String toString() {
        return "BPP{" +
                "size=" + Arrays.toString(size) +
                ", N=" + N +
                ", C=" + C +
                '}';
    }
}
