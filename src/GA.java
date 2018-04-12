import java.util.ArrayList;
import java.util.Collections;

/**Algoritmo genetico*/
public class GA implements Solver {
    private BPP bpp;
    private Sol bestSol;
    private int popSize;
    private double muteRatio;
    private int k;

    /**@param popSize tamanho da população
     * @param muteRatio probabilidade de se aplicar uma mutação
     * @param k número de membros por sorteio */
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


    /**Inicializa a população*/
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

    @Override
    public String toString() {
        return "GA{" +
                "popSize=" + popSize +
                ", muteRatio=" + muteRatio +
                ", k=" + k +
                '}';
    }
}
