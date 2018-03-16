/**
 * Variable Neighborhood Search
 */
public class VNS implements Solver {
    private BPP bpp;
    private Sol bestSol;
    private int idx[];
    public int ite, v, passo;


    /**
     * @param ite número de iterações
     * @param v número de vizinhancas
     * @param passo quantidade de pacotes eliminados
     *              que aumenta a cada nova vizinhança.
     */
    public VNS(int ite, int v, int passo) {
        this.ite = ite;
        this.v = v;
        this.passo = passo;
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
    public String toString() {
        return "VNS{" +
                "ite=" + ite +
                ", v=" + v +
                ", passo=" + passo +
                '}';
    }

    @Override
    public int run() {
        Sol current = new Sol(bpp);
        VND vnd = new VND(bpp, current);

        Utils.shuffler(idx);
        current.bestFitRandom(idx);
        int best = vnd.run();

        bestSol.copy(current);
        int k = 2;
        int cont = 0;
        while (cont < v) {
            for (int i = 1; i < ite; i++) {
                pertub(k, current, bestSol);
                int x = vnd.run();
                if (x < best) {
                    System.out.println(k + " VNS: " + x);
                    best = x;
                    bestSol.copy(current);
                    k = 2;
                    cont=0;
                    i = -1;
                }
            }
            cont++;
            k+=passo;
        }
//        System.out.println(best);
        return best;
    }



    @Override
    public Sol getSol() {
        return bestSol;
    }

}
