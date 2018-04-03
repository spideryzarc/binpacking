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
            pertub(curr,aux,count);
            int xcount = aux.binCount();
            double x = FA(aux,xcount);
            if(x > currFA || xcount < count){
                curr.copy(aux);
                currFA = x;
                count = xcount;
                if(bestCount > count){
                    bestSol.copy(curr);
                }
            }else if(Utils.rd.nextDouble() < P(x-currFA,T)){
                curr.copy(aux);
                currFA = x;
            }
            System.out.println(T + " "+currFA);
        }



        return bestSol.binCount();
    }

    private double P(double delta, double t) {
        return Math.exp(delta/t);
    }

    private void pertub(Sol curr, Sol aux,int count) {
        aux.copy(curr);
        int load[] = new int[count];
        for (int i = 0; i < bpp.N; i++)
            load[curr.binOf[i]] += bpp.size[i];
        if(true || Utils.rd.nextBoolean()){ //um item troca de pacote
            while(true) {
                int pack = Utils.rd.nextInt(count);
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
                        return;
                    }
                }
            }

        }else{

        }
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
