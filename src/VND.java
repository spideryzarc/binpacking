/**Variable Neighborhood Descendent*/
public class VND {
    BPP bpp;
    Sol sol;

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

    boolean move1(){
        int count = sol.binCount();
        int load[] = new int[count];
        int binof[] = sol.binOf;
        int size[] = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        double avg = (double) bpp.sizeSum / count;
        double a = stdDev(load, avg);

        boolean moved;

        for (int i = 0; i < binof.length; i++) {
            int bi = binof[i];
            for (int j = 0; j < count; j++)
                if (bi != j && load[j] + size[i] <= bpp.C) {
                    load[j] += size[i];
                    load[bi] -= size[i];
                    double x = stdDev(load, avg);
                    if (x > a) {
                        binof[i] = j;
                        a = x;

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
                            a = stdDev(load, avg);

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

    boolean move2(){

        return false;
    }

    boolean move3(){

        return false;
    }

    int run(){

        boolean flag;
        do{
            flag = move1();
            if(!flag)
                flag = move2();
            if(!flag)
                flag = move3();

            //System.out.println("VND: ");

        }while(flag);

        return sol.binCount();
    }
}
