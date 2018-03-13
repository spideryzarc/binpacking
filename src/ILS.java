import static java.util.Arrays.fill;

/**Iterated Local Search*/
public class ILS {
    BPP bpp;
    Sol sol;
    int idx[];

    public ILS(BPP bpp, Sol sol) {
        this.bpp = bpp;
        this.sol = sol;
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;

    }

    public int bestFit(Sol current) {
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

    private void pertub(int k,Sol current, Sol best){
        current.copy(best);
        int n = current.binCount();
        for (int i = 0; i < k; i++) {
            int x = Utils.rd.nextInt(n);
            for (int j = 0; j < bpp.N; j++)
                if(current.binOf[j] == x)
                    current.binOf[j] = -1;
                else if(current.binOf[j] > x)
                    current.binOf[j]--;
        }
        bestFit(current);
    }

    public int run(int ite, int k) {
        Sol current = new Sol(bpp);
        HillClimbing hc = new HillClimbing(bpp, current);

        Utils.shuffler(idx);
        current.bestFitRandom(idx);
        int best = hc.run();

        sol.copy(current);

        for (int i = 1; i < ite; i++) {

            pertub(k,current,sol);

            int x = hc.run();

            if (x < best) {
                best = x;
                sol.copy(current);
                System.out.println(i + " ILS: " + x);
                //i = -1;
            }
        }
        System.out.println(best);
        return best;
    }

}
