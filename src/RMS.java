public class RMS implements Solver {
    private BPP bpp;
    private Sol bestSol;
    private int idx[];
    int ite = 100;

    @Override
    public String toString() {
        return "RMS{" +
                "ite=" + ite +
                '}';
    }

    /**
     * @param ite numero de iterações
     */

    public RMS(int ite) {
        this.ite = ite;
    }

    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        this.bestSol = new Sol(bpp);
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
    }

    public Sol getSol() {
        return bestSol;
    }

    public int run() {
        Sol current = new Sol(bpp);
        HillClimbing hc = new HillClimbing(bpp, current);
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < ite; i++) {

            Utils.shuffler(idx);
            current.bestFitRandom(idx);
            int x = hc.run();

            if (x < best) {
                best = x;
                bestSol.copy(current);
                //System.out.println(i + " RMS: " + x);
                //i = -1;
            }
        }
//        System.out.println(best);
        return best;
    }

}
