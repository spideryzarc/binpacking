import java.util.ArrayList;
import java.util.Collections;

public class GA implements Solver{
    BPP bpp;
    Sol bestSol;
    int popSize;
    double muteRatio;
    int k;

    public GA(int popSize, double muteRatio, int k) {
        this.popSize = popSize;
        this.muteRatio = muteRatio;
        this.k = k;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
        pop = new ArrayList<>();
    }


    ArrayList<Sol> pop;

    @Override
    public int run() {
        popIni();
        Collections.sort(pop);




        bestSol.copy(pop.get(0));
        return bestSol.count;
    }


    private void popIni() {
        pop.clear();
        int idx[] = new int[bpp.N];
        for (int i = 0; i < bpp.N; i++)
            idx[i] = i;
        Utils.shuffler(idx);
        for (int i = 0; i < popSize; i++) {
            Utils.shuffler(idx);
            Sol sol = new Sol(bpp);
            sol.bestFitRandom(idx);
            sol.updateAtributes();
            pop.add(sol);
        }
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
