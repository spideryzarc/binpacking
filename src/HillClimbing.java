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
//    public int fa(int[] load) {
//        int min = load[0];
//        for (int i = 1; i < load.length; i++)
//            if (min > load[i])
//                min = load[i];
//        return min;
//    }

    public double fa(int[] load, double avg) { // desvio
        double soma = 0;
        for (int i = 0; i < load.length; i++)
            soma+= (load[i]-avg)*(load[i]-avg);
        return -soma;
    }

    public int run() {
        int count = sol.binCount();
        int load[] = new int[count];
        int binof[] = sol.binOf;
        int size[] = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        double avg = (double)bpp.sizeSum/count;
        double a = fa(load,avg);

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
                        load[j] += size[i];
                        load[bi] -= size[i];
                        double x = fa(load,avg);
                        if (x < a) {
                            binof[i] = j;
                            a = x;
//                            System.out.println("fa: " + a);
                            if (load[bi] == 0) {
                                //pacote vazio
//                                System.out.println("Oba!");
                                for (int k = 0; k < binof.length; k++)
                                    if (binof[k] > bi)
                                        binof[k]--;
                                count--;
                                load = new int[count];
                                for (int k = 0; k < binof.length; k++)
                                    load[binof[k]] += size[k];
                                avg = (double)bpp.sizeSum/count;
                                a = fa(load,avg);
                            }
                            moved = true;
                            break ls;
                        }else{
                            load[j] -= size[i];
                            load[bi] += size[i];
                        }
                    }
            }
        } while (moved);




        return count;
    }

}