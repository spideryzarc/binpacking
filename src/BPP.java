import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

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

    /**abrir arquivo path*/
    public BPP(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(path));
        N = sc.nextInt();
        C = sc.nextInt();
        size = new int[N];
        for (int i = 0; i < N; i++)
            size[i] = sc.nextInt();

        sc.close();
    }

    public BPP() {
    }
}
