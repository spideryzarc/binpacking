import static java.lang.Math.abs;

/**
 * Variable Neighborhood Descendent
 */
public class VND {

    private BPP bpp;
    private Sol sol;
    private int bidx[], idx[], lb;

    public VND(BPP bpp, Sol sol) {
        this.bpp = bpp;
        this.sol = sol;
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;
        int lb = bpp.LB();

    }

    /**
     * função de avaliação
     */
//    public double stdDev() { // desvio
//        double soma = 0;
//        for (int i = 0; i < load.length; i++) {
//            double x = (load[i] - avg);
//            soma += x * x;
//        }
//        return soma;
//    }
    public double stdDevOptDelta(int i, int bi, int bj) { // desvio
        return size[i] * 2 * (load[bj] + size[i] - load[bi]);
    }


    /**
     * um item troca de pacote
     */
    boolean move1() {

        for (int i : idx) {
            int bi = binof[i];
            for (int bj : bidx)
                if (bi != bj && load[bj] + size[i] <= bpp.C) {
                    double x = stdDevOptDelta(i, bi, bj);
                    if (x > Utils.eps) {
                        load[bj] += size[i];
                        load[bi] -= size[i];
                        binof[i] = bj;
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

//                            System.out.println("mv1: " + count);
                            bidx = new int[count];
                            for (int z = 0; z < count; z++)
                                bidx[z] = z;
                        }
//                        System.out.println("mv1: " + x);
                        return true;
                    }
                }
        }


        return false;
    }


    public double stdDevOptDelta(int i, int bi, int j, int bj) { // desvio
        return 2 * (size[i] * (load[bj] + size[i] - 2 * size[j]) + size[j] * (size[j] - load[bj]) + load[bi] * (size[j] - size[i]));
    }

    /**
     * dois itens em pacotes diferentes trocam de lugar entre si
     */
    boolean move2() {
        final int N = binof.length;
        for (int a = 0; a < N; a++) {
            int i = idx[a];
            for (int b = a + 1; b < N; b++) {
                int j = idx[b];
                if (binof[i] != binof[j] &&
                        load[binof[i]] - size[i] + size[j] <= bpp.C &&
                        load[binof[j]] - size[j] + size[i] <= bpp.C) {

                    double x = stdDevOptDelta(i, binof[i], j, binof[j]);

                    if (x > Utils.eps) {
                        load[binof[i]] += -size[i] + size[j];
                        load[binof[j]] += -size[j] + size[i];
                        int aux = binof[i];
                        binof[i] = binof[j];
                        binof[j] = aux;
//                        System.out.println("MV2 "+x);
                        return true;
                    }
                }
            }

        }

        return false;
    }

    boolean move3() {

        return false;
    }

    private int count;
    private int load[];
    private int binof[];
    private int size[];


    int run() {

        count = sol.binCount();
        load = new int[count];
        binof = sol.binOf;
        size = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];

        bidx = new int[count];
        for (int i = 0; i < count; i++)
            bidx[i] = i;


        //VND

        boolean flag;
        do {
            Utils.shuffler(bidx);
            Utils.shuffler(idx);

            flag = move1();
            if (!flag)
                flag = move2();
            if (!flag)
                flag = move3();


//            System.out.println("VND: "+dev);
        } while (flag && count > lb);


        return sol.binCount();
    }
}
