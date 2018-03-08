import java.util.Arrays;

import static java.util.Arrays.fill;
import static java.util.Arrays.stream;

public class HillClimbing {
    final BPP bpp;
    Sol sol;

    public HillClimbing(BPP bpp, Sol sol) {
        this.bpp = bpp;
        this.sol = sol;
    }

    /**
     * função de avaliação
     */
    public int fa(int[] load) {
        int min = load[0];
        for (int i = 1; i < load.length; i++)
            if (min > load[i])
                min = load[i];
        return min;
    }

    public int run() {
        int count = sol.binCount();
        int load[] = new int[count];
        int binof[] = sol.binOf;
        int size[] = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        int a = fa(load);

        boolean moved;
        //hillclimb
        do {
            moved = false;
            //busca local
            ls:
            for (int i = 0; i < binof.length; i++) {
                int bi = binof[i];
                for (int j = 0; j < count; j++)
                    if (bi != j && load[j] + size[i] <= bpp.C) {
                        int x = load[bi] - size[i];
                        if (x < a) {
                            load[j] += size[i];
                            load[bi] -= size[i];
                            binof[i] = j;
                            a = x;
                            System.out.println("fa: " + a);
                            if (a == 0) {
                                //pacote vazio
                                System.out.println("Oba!");
                                for (int k = 0; k < binof.length; k++)
                                    if (binof[k] > bi)
                                        binof[k]--;
                                count--;
                                load = new int[count];
                                for (int k = 0; k < binof.length; k++)
                                    load[binof[k]] += size[k];
                                a = fa(load);
                            }
                            moved = true;
                            break ls;
                        }
                    }
            }
        } while (moved);

        System.out.println(Arrays.toString(load));


        return a;
    }

}