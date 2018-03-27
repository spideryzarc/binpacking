import static java.util.Arrays.fill;

public class GLS implements Solver{
    Sol bestSol;
    BPP bpp;
    int ite, idx[], p[][];


    public GLS(int ite) {
        this.ite = ite;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++)
            idx[i]=i;
        p = new int[bpp.N][];
        for (int i = 0; i < idx.length; i++)
            p[i] = new int[i];
    }

    int load[];
    @Override
    public int run() {
        //constroi solução inicial
        Sol sol = new Sol(bpp);
        Utils.shuffler(idx);
        int count = sol.bestFitRandom(idx);
        bestSol.copy(sol);
        load = new int[count];
        for (int i = 0; i < sol.binOf.length; i++)
            load[sol.binOf[i]] += bpp.size[i];

        for (int i = 0; i < ite; i++) {
            hc(sol);
            if(sol.binCount() < bestSol.binCount()){
                bestSol.copy(sol);
                System.out.println("GLS: "+bestSol.binCount());
                penaltyReset();
            }

            updateMatriz(sol);

        }

        return bestSol.binCount();
    }

    private void penaltyReset() {
        for (int i = 0; i < idx.length; i++)
            fill(p[i],0);
    }


    private void updateMatriz(Sol sol) {
        int k = 0;
//        int min = load[0];
//        for (int i = 1; i < load.length; i++)
//        if(min > load[i]){
//            k = i;
//            min = load[i];
//        }
        k = Utils.rd.nextInt(load.length);

        for (int i = 0; i < bpp.N; i++)
            if(sol.binOf[i] == k){
                for (int j = 0; j < i; j++)
                    if(sol.binOf[j] == k){
                        p[i][j]++;
                    }
            }
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }

    public double stdDev(int[] load, double avg) { // desvio
        double soma = 0;
        for (int i = 0; i < load.length; i++) {
            double x = (load[i] - avg);
            soma += x * x;
        }
        return soma;
    }



    public int hc(Sol sol) {
        int count = sol.binCount();

        int binof[] = sol.binOf;
        int size[] = bpp.size;

        double avg = (double) bpp.sizeSum / count;
        double a = stdDev(load, avg) * penalty(sol);

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
                        binof[i] = j;
                        double x = stdDev(load, avg) * penalty(sol);
                        if (x > a) {// se mover i para j melhora a solução
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
                                a = stdDev(load, avg)* penalty(sol);

                            }
                            moved = true;
                            break ls;
                        } else {
                            load[j] -= size[i];
                            load[bi] += size[i];
                            binof[i] = bi;
                        }
                    }
            }
        } while (moved);


        return count;
    }

    private double penalty(Sol sol) {
        int s =0;
        for (int i = 0; i < bpp.N; i++) {
            for (int j = 0; j < i; j++)
            if(sol.binOf[i] ==sol.binOf[j]){
                s+=p[i][j];
            }
        }
        return Math.exp(-s);
    }
}
