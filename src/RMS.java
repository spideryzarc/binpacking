public class RMS {
    BPP bpp;
    Sol sol;
    int idx[];

    public RMS(BPP bpp, Sol sol) {
        this.bpp = bpp;
        this.sol = sol;
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++) {
            idx[i]=i;
        }
    }

    public int run(int ite){
        Sol current = new Sol(bpp);
        HillClimbing hc = new HillClimbing(bpp,current);
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < ite; i++) {

            Utils.shuffler(idx);
            int x = current.bestFitRandom(idx);
//            System.out.println("bfr: "+x);
            x = hc.run();
//            System.out.println("hc: "+x);

            if(x < best){
                best = x;
                sol.copy(current);
                System.out.println("RMS: "+x);
            }
        }
        System.out.println(best);
        return best;
    }

}
