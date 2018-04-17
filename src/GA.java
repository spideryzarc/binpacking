import java.util.ArrayList;
import java.util.Collections;

/**
 * Algoritmo genetico
 */
public class GA implements Solver {
    private BPP bpp;
    private Sol bestSol;
    private final int popSize;
    private double muteRatio;
    private final int k;
    private int idx[];
    private int ite;

    /**
     * @param popSize   tamanho da população
     * @param muteRatio probabilidade de se aplicar uma mutação
     * @param k         número de membros por torneio
     * @param ite       numero de iterações
     */
    public GA(int popSize, double muteRatio, int k, int ite) {
        this.ite = ite;
        this.popSize = popSize;
        this.muteRatio = muteRatio;
        this.k = k;
        idx = new int[popSize];
        for (int i = 0; i < popSize; i++)
            idx[i] = i;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
        pop = new ArrayList<>();
    }


    ArrayList<Sol> pop;

    private Sol[] select() {
        Utils.shuffler(idx);
        Sol dad = pop.get(0);
        for (int i = 1; i < k; i++)
            if (pop.get(i).compareTo(dad) < 0) // i é melhor que dad
                dad = pop.get(i);

        Utils.shuffler(idx);
        Sol mom = null;
        for (int i = 0; i < k; i++)
            if (pop.get(i) != dad
                    && (mom == null || pop.get(i).compareTo(mom) < 0)) // i é melhor que dad
                mom = pop.get(i);

        return new Sol[]{dad, mom};
    }

    @Override
    public int run() {
        popIni();

        for (int i = 0; i < ite; i++) {
            Collections.sort(pop);
            while (pop.size() > popSize)
                pop.remove(pop.size()-1);

            Sol parents[] = select();

            Sol son1 = crossover(parents[0], parents[1]);
            if (son1 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutate(son1);
                pop.add(son1);
            }


            Sol son2 = crossover(parents[1], parents[0]);
            if (son2 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutate(son2);
                pop.add(son2);
            }

        }
        bestSol.copy(pop.get(0));
        return bestSol.count;
    }

    private void mutate(Sol son1) {

    }


    private Sol crossover(Sol dad, Sol mom) {
        int a = bpp.N / 3;
        int b = 2 * a;
        Sol son = new Sol(bpp);
        for (int i = a; i < b; i++)
            son.binOf[i] = dad.binOf[i];
        int count = Math.max(mom.count, dad.count);
        int load[] = new int[count];
        for (int i = a; i < b; i++)
            load[son.binOf[i]] += bpp.size[i];

        for (int i = 0; i < a; i++) {
            if (load[mom.binOf[i]] + bpp.size[i] <= bpp.C) {
                son.binOf[i] = mom.binOf[i];
                load[son.binOf[i]] += bpp.size[i];
            } else {
                boolean flag = true;
                for (int j = 0; j < count; j++) {
                    if (load[j] + bpp.size[i] <= bpp.C) {
                        load[j] += bpp.size[i];
                        son.binOf[i] = j;
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return null;
                }
            }
        }
        for (int i = 0; i < count; i++) {
            if(load[i] == 0){

                //elemina pacote vazio
                for (int k = 0; k < son.binOf.length; k++)
                    if (son.binOf[k] > i)
                        son.binOf[k]--;
                count--;
                //System.err.println(count);
                break;
            }
        }
        son.updateAtributes();
        return son;
    }


    /**
     * Inicializa a população
     */
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
