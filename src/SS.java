import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Scatter Search
 */
public class SS implements Solver {
    private BPP bpp;
    private Sol bestSol;
    public int popSize;
    public int eliteSize;
    private int idx[];

    @Override
    public String toString() {
        return "SS{" +
                "popSize=" + popSize +
                ", eliteSize=" + eliteSize +
                '}';
    }

    public SS(int popSize, int eliteSize) {
        this.popSize = popSize;
        this.eliteSize = eliteSize;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        idx = new int[bpp.N];
        for (int i = 0; i < bpp.N; i++)
            idx[i] = i;

    }

    private void initPop(ArrayList<Sol> popini) {
        for (int i = 0; i < popSize; i++) {
            Sol sol = new Sol(bpp);
            VND vnd = new VND(bpp, sol);
            Utils.shuffler(idx);
            sol.bestFitRandom(idx);
            int x = vnd.run();
            if (!popini.contains(sol)) {
                sol.updateAtributes();
                popini.add(sol);
            }
        }
    }


    @Override
    public int run() {
        ArrayList<Sol> popini = new ArrayList<>();
        initPop(popini);
        ArrayList<Sol> elite = new ArrayList<>();
        eliteSelect(popini, elite);
        popini = null;

        boolean alterou;
        do {
            Collections.sort(elite);
            alterou = false;
            fori:
            for (int i = 1; i < elite.size(); i++) {
                Sol si = elite.get(i);
                for (int j = 0; j < i; j++) {
                    Sol sj = elite.get(j);

                    Sol son = GA.crossover(si, sj);
                    if (son != null
                            && son.compareTo(elite.get(elite.size() - 1)) < 0
                            && !elite.contains(son)) {
                        elite.set(elite.size() - 1, son);
                        alterou = true;
                        break fori;
                    }

                    son = GA.crossover(sj, si);
                    if (son != null
                            && son.compareTo(elite.get(elite.size() - 1)) < 0
                            && !elite.contains(son)) {
                        elite.set(elite.size() - 1, son);
                        alterou = true;
                        break fori;
                    }
                }
            }

            System.out.println(elite);
        } while (alterou);

        bestSol = elite.get(0);
        return bestSol.binCount;
    }

    private void eliteSelect(ArrayList<Sol> popini, ArrayList<Sol> elite) {
        Sol pivot = popini.get(0);
        for (Sol sol : popini)
            if (sol.compareTo(pivot) < 0)
                pivot = sol;

        elite.add(pivot);
        int dist[] = new int[popini.size()];
        int max = 0, maxarg = -1;
        for (int i = 0; i < dist.length; i++) {
            dist[i] = pivot.dist(popini.get(i));
            if (max < dist[i]) {
                max = dist[i];
                maxarg = i;
            }
        }

        for (int k = 1; k < eliteSize; k++) {
            pivot = popini.get(maxarg);
            elite.add(pivot);
            max = 0;
            for (int i = 0; i < dist.length; i++) {
                dist[i] = Math.min(dist[i], pivot.dist(popini.get(i)));
                if (max < dist[i]) {
                    max = dist[i];
                    maxarg = i;
                }
            }
        }
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
