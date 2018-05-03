import java.util.ArrayList;
import java.util.Collections;

/**
 * Scatter Search with Path Relinking
 */
public class SSwPR extends SS {

    public SSwPR(int popSize, int eliteSize) {
        super(popSize, eliteSize);
    }

    @Override
    public int run() {
        ArrayList<Sol> popini = new ArrayList<>();
        initPop(popini);
        ArrayList<Sol> elite = new ArrayList<>();
        eliteSelect(popini, elite);
        popini = null;
        Collections.sort(elite);

        boolean alterou;
        do {
            alterou = false;
            fori:
            for (int i = 1; i < eliteSize; i++) {
                Sol si = elite.get(i);
                for (int j = 0; j < i; j++) {
                    Sol sj = elite.get(j);

                    if (relinking(si, sj, elite)) {
                        alterou = true;
                        // break fori;
                    }
                }
            }
            if (alterou) {
                Collections.sort(elite);
                while (elite.size() > eliteSize)
                    elite.remove(elite.size() - 1);
            }
            System.out.println(elite);
        } while (alterou);

        bestSol = elite.get(0);
        return bestSol.binCount;
    }

    private boolean relinking(Sol si, Sol sj, ArrayList<Sol> elite) {
        boolean added = false;
        boolean moved;
        Sol inter = new Sol(bpp);
        inter.copy(si);
        step:
        do {
            moved = false;
            Utils.shuffler(idx);
            int min = Integer.MAX_VALUE;
            int arg = -1;
            for (int i : idx)
                if (inter.binOf[i] != sj.binOf[i]) {
                    int interBi = inter.binOf[i];
                    inter.load[sj.binOf[i]] += bpp.size[i];
                    inter.load[interBi] -= bpp.size[i];
                    inter.binOf[i] = sj.binOf[i];

                    int violation = inter.violation();
                    if (violation == 0) {
                        if (inter.load[interBi] == 0) {
                            System.err.println("oba!");
                            //elemina pacote vazio
                            for (int k = 0; k < inter.binOf.length; k++)
                                if (inter.binOf[k] > interBi)
                                    inter.binOf[k]--;
                            inter.updateAtributes();
                        }
                        inter.updateAtributes();
                        if (!elite.contains(inter)
                                && inter.compareTo(elite.get(eliteSize - 1)) < 0) {
                            Sol aux = new Sol(bpp);
                            aux.copy(inter);


                            elite.add(aux);
                            added = true;
                        }
                        moved = true;
                        continue step;
                    } else {//guarda o menos violado
                        if (min > violation) {
                            min = violation;
                            arg = i;
                        }
                    }

                    inter.load[sj.binOf[i]] -= bpp.size[i];
                    inter.load[interBi] += bpp.size[i];
                    inter.binOf[i] = interBi;

                }
            if (!moved) {
                int i = arg;
                int interBi = inter.binOf[i];
                inter.load[sj.binOf[i]] += bpp.size[i];
                inter.load[interBi] -= bpp.size[i];
                inter.binOf[i] = sj.binOf[i];
            }

        } while (!inter.equals(sj));


        return added;
    }
}
