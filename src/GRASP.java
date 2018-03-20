import static java.util.Arrays.fill;

public class GRASP implements Solver {
    private BPP bpp;
    private Sol bestSol;
    int k, ite;
    int candidato[];

    public GRASP(int k, int ite) {
        this.k = k;
        this.ite = ite;
        candidato = new int[k];
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
    }


    @Override
    public String toString() {
        return "GRASP{" +
                "k=" + k +
                ", ite=" + ite +
                '}';
    }

    private int greedyRandom(Sol current){

        int binOf[] = current.binOf;
        fill(binOf, -1);
        int binCount = 0;

        for (int i = 0; i < bpp.N; i++) {
            if (binOf[i] == -1) {
                int residuo = bpp.C - bpp.size[i];
                binOf[i] = binCount;
                int s;
                do{
                    s=0;

                    for (int j = i + 1; j < bpp.N && s < k; j++) {
                        if (binOf[j] == -1 && bpp.size[j] <= residuo) {
                            candidato[s] = j;
                            s++;
                        }
                    }
                    if(s>0) {
                        int x = Utils.rd.nextInt(s);
                        binOf[x] = binCount;
                        residuo -= bpp.size[x];
                    }
                }while(s>1);
                binCount++;
            }
        }
        return binCount;
    }

    @Override
    public int run() {
        Sol current = new Sol(bpp);
        VND vnd = new VND(bpp, current);


        greedyRandom(current);
        int best = vnd.run();

        bestSol.copy(current);

        for (int i = 1; i < ite; i++) {

            greedyRandom(current);
            int x = vnd.run();

            if (x < best) {
                best = x;
                bestSol.copy(current);
                System.out.println(i + " GRASP: " + x);
            }
        }
        return best;
    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
