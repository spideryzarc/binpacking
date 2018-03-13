/**
 * Variable Neighborhood Descendent
 */
public class VND {

    private BPP bpp;
    private Sol sol;

    public VND(BPP bpp, Sol sol) {
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

    /**
     * um item troca de pacote
     */
    boolean move1() {


        for (int i = 0; i < binof.length; i++) {
            int bi = binof[i];
            for (int j = 0; j < count; j++)
                if (bi != j && load[j] + size[i] <= bpp.C) {
                    load[j] += size[i];
                    load[bi] -= size[i];
                    double x = stdDev(load, avg);//dá pra otimizar depois
                    if (x > dev) {
                        binof[i] = j;
                        dev = x;
                        if (load[bi] == 0) { //pacote vazio

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
                            dev = stdDev(load, avg);

                            System.out.println("mv1: " + count);
                        }
                        return true;
                    } else {
                        load[j] -= size[i];
                        load[bi] += size[i];
                    }
                }
        }


        return false;
    }

    /**
     * dois itens em pacotes diferentes trocam de lugar entre si
     */
    boolean move2() {

        return false;
    }

    boolean move3() {

        return false;
    }

    private int count;
    private int load[];
    private int binof[];
    private int size[];
    private double avg, dev;

    int run() {

        count = sol.binCount();
        load = new int[count];
        binof = sol.binOf;
        size = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        avg = (double) bpp.sizeSum / count;
        dev = stdDev(load, avg);

        boolean flag;
        do {
            flag = move1();
            if (!flag)
                flag = move2();
            if (!flag)
                flag = move3();

            //System.out.println("VND: ");

        } while (flag);

        return sol.binCount();
    }
}
