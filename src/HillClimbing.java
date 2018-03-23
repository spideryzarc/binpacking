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
    public double stdDev(int[] load, double avg) { // desvio
        double soma = 0;
        for (int i = 0; i < load.length; i++) {
            double x = (load[i] - avg);
            soma += x * x;
        }
        return soma;
    }

    public int run() {
        int count = sol.binCount();
        int load[] = new int[count];
        int binof[] = sol.binOf;
        int size[] = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        double avg = (double) bpp.sizeSum / count;
        double a = stdDev(load, avg);

        boolean moved;
        //hillclimb
        do {
            moved = false;
            //busca local
            ls:
            for (int i = 0; i < binof.length; i++) {//para todo_ item i
                int bi = binof[i];
                for (int j = 0; j < count; j++)//para todo_ pacote j
                    if (bi != j && load[j] + size[i] <= bpp.C) {//se ele i cabe em j
                        load[j] += size[i];
                        load[bi] -= size[i];
                        double x = stdDev(load, avg);
                        if (x > a) {// se mover i para j melhora a solução
                            binof[i] = j;
                            a = x;

                            if (load[bi] == 0) { //se  pacote bi ficou vazio

                                //elemina pacote vazio
                                for (int k = 0; k < binof.length; k++)
                                    if (binof[k] > bi)
                                        binof[k]--;
                                count--;

                                //atualiza vetor load
                                load = new int[count];
                                for (int k = 0; k < binof.length; k++)
                                    load[binof[k]] += size[k];

                                //atualiza média e avaliação corrente
                                avg = (double) bpp.sizeSum / count;
                                a = stdDev(load, avg);

                            }
                            moved = true;
                            break ls;
                        } else {
                            load[j] -= size[i];
                            load[bi] += size[i];
                        }
                    }
            }
        } while (moved);


        return count;
    }

}