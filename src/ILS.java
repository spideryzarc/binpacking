import static java.util.Arrays.fill;

/**
 * Iterated Local Search
 */
public class ILS implements Solver {
    private int k;
    private BPP bpp;
    private Sol bestSol;
    private int idx[];
    public int ite;


    /**
     * @param ite número de iterações
     * @param k   número de pacotes alterados a cada nova iteração
     */
    public ILS(int ite, int k) {
        this.ite = ite;
        this.k = k;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        this.bestSol = new Sol(bpp);
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;
    }

    /**
     * empacote os itens sem pacote (binof == -1) em ordem randomica
     */
    public int fit(Sol current) {
        int binOf[] = current.binOf;
        int binCount = current.binCount();
        Utils.shuffler(idx);
        for (int a = 0; a < bpp.N; a++) {
            int i = idx[a];
            if (binOf[i] == -1) {
                int residuo = bpp.C - bpp.size[i];
                binOf[i] = binCount;
                for (int b = a + 1; b < bpp.N; b++) {
                    int j = idx[b];
                    if (binOf[j] == -1 && bpp.size[j] <= residuo) {
                        binOf[j] = binCount;
                        residuo -= bpp.size[j];
                    }
                }
                binCount++;
            }
        }
        return binCount;
    }

    /**
     * Elimina k pacotes aleatórios e reempacotas seus itens em ordem aleatória
     *
     * @param k       numero de pacotes eliminados
     * @param current saída com solução best pertubada
     * @param best    solução de entrada
     */
    private void pertub(int k, Sol current, Sol best) {
        current.copy(best);
        int n = current.binCount();
        for (int i = 0; i < k; i++) {
            int x = Utils.rd.nextInt(n);
            for (int j = 0; j < bpp.N; j++)
                if (current.binOf[j] == x)
                    current.binOf[j] = -1;
                else if (current.binOf[j] > x)
                    current.binOf[j]--;
        }
        fit(current);
    }

    @Override
    public int run() {
        Sol current = new Sol(bpp);
        HillClimbing hc = new HillClimbing(bpp, current);

        Utils.shuffler(idx);
        current.bestFitRandom(idx);
        int best = hc.run();

        bestSol.copy(current);

        for (int i = 1; i < ite; i++) {

            pertub(k, current, bestSol);

            int x = hc.run();

            if (x < best) {
                best = x;
                bestSol.copy(current);
//                System.out.println(i + " ILS: " + x);
                //i = -1;
            }
        }
//        System.out.println(best);
        return best;
    }

    @Override
    public String toString() {
        return "ILS{" +
                "k=" + k +
                ", ite=" + ite +
                '}';
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }

}
