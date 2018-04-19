import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
/**os itens devem estar ordenados em ordem decrescente*/
public class BPP {
    /**
     * tamanho de cada item
     */
    int size[];
    /**
     * quantidade de itens
     */
    int N;
    /**
     * capacidade dos pacotes
     */
    int C;

    /**Soma dos pesos dos itens */
    int sizeSum;

    @Override
    public String toString() {
        return "BPP{" +
                "N=" + N +
                ", C=" + C +
                ", size=" + Arrays.toString(size) +
                '}';
    }

    /**
     * abrir arquivo path
     */
    public BPP(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(path));
        N = sc.nextInt();
        C = sc.nextInt();
        size = new int[N];
        sizeSum = 0;
        for (int i = 0; i < N; i++) {
            size[i] = sc.nextInt();
            sizeSum+=size[i];
        }

        sc.close();
    }

    public BPP() {
    }

    /**
     * limite inferior para o numero de pacotes
     */
    public int LB() {
        int s = 0;
        for (int i = 0; i < N; i++)
            s += size[i];
        return (int) Math.ceil((double) s / C);
    }

}
