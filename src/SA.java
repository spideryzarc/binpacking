/**Simulated Annealing*/
public class SA implements Solver{
    private BPP bpp;
    public Sol bestSol;
    double t0,tf,l;

    public SA(double t0, double tf, double l) {
        this.t0 = t0;
        this.tf = tf;
        this.l = l;
    }

    @Override
    public String toString() {
        return "SA{" +
                "t0=" + t0 +
                ", tf=" + tf +
                ", l=" + l +
                '}';
    }



    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
    }

    private double FA(Sol sol, int count){
        int load[] = new int[count];
        int binof[] = sol.binOf;
        int size[] = bpp.size;
        for (int i = 0; i < binof.length; i++)
            load[binof[i]] += size[i];
        double avg = (double) bpp.sizeSum / count;
        return Utils.stdDev(load, avg);
    }

    @Override
    public int run() {
        Sol curr = new Sol(bpp);
        int count = curr.bestFit();
        bestSol = new Sol(bpp);
        bestSol.copy(curr);
        double currFA = FA(curr,count);
        int bestCount = count;

        Sol aux = new Sol(bpp);
        for(double T = t0; T > tf; T*=l){
            if(!pertub(curr,aux,count))
                continue;
            int xcount = aux.binCount();
            double x = FA(aux,xcount);
            if(x > currFA || xcount < count){
                curr.copy(aux);
                currFA = x;
                count = xcount;
                if(bestCount > count){
                    bestCount = count;
                    bestSol.copy(curr);
                    System.out.println("SA: "+count);
                    T = t0;
                }
                //System.out.println(T + " "+currFA);
            }else if(Utils.rd.nextDouble() < P(x-currFA,T)){
                curr.copy(aux);
                currFA = x;
                //System.out.println(T + " "+currFA+" *");
            }


        }



        return bestSol.binCount();
    }

    private double P(double delta, double t) {
        return Math.exp(delta/t);
    }

    private boolean pertub(Sol curr, Sol aux,int count) {
        aux.copy(curr);
        int load[] = new int[count];
        for (int i = 0; i < bpp.N; i++)
            load[curr.binOf[i]] += bpp.size[i];
        if( Utils.rd.nextBoolean()){ //um item troca de pacote
            int idx[] = new int[count];
            for (int i = 0; i < count; i++)
                idx[i] = i;
            Utils.shuffler(idx);

            for(int pack : idx){
                //int pack = Utils.rd.nextInt(binCount);
                int sobra = bpp.C - load[pack];
                int x;
                for (x = bpp.N - 1; x >= 0 && bpp.size[x] <= sobra; x--) {
                    //nada
                }
                int s = bpp.N - x - 1;
                if (s > 0) {
                    int item = Utils.rd.nextInt(s) + x + 1;
                    if (curr.binOf[item] != pack) {
                        int bi = curr.binOf[item];
                        aux.binOf[item] = pack;
                        load[bi]-= bpp.size[item];
                        //se o pacote do item esvaziar
                        if(load[bi] == 0){
                            //elemina pacote vazio
                            for (int k = 0; k < bpp.N; k++)
                                if (aux.binOf[k] > bi)
                                    aux.binOf[k]--;
                        }
                        return true;
                    }
                }
            }

        }else{ // dois itens trocam de pacotes entre si
            int idx[] = new int[bpp.N];
            for (int i = 0; i < bpp.N; i++)
                idx[i] = i;
            Utils.shuffler(idx);
            for (int a = 0; a < bpp.N; a++) {
                int i = idx[a];
                int bi = curr.binOf[i];
                for (int b = 0; b < a; b++) {
                    int j = idx[b];
                    int bj = curr.binOf[j];

                    if(bi!=bj
                            && load[bi]-bpp.size[i]+bpp.size[j]<= bpp.C
                            && load[bj]-bpp.size[j]+bpp.size[i]<= bpp.C){
                        aux.binOf[i] = bj;
                        aux.binOf[j] = bi;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
