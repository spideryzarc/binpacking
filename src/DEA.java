import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.fill;

/**
 * Algoritmo de estimação de distribuição
 */
public class DEA implements Solver {
    BPP bpp;
    Sol bestSol;
    private double[][] D, P;
    int sampleSize;
    int subSampleSize;
    double alpha;
    int ite;
    private int idx[];

    public DEA(int sampleSize, int subSampleSize, double alpha, int ite) {
        this.sampleSize = sampleSize;
        this.subSampleSize = subSampleSize;
        this.alpha = alpha;
        this.ite = ite;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
        bestSol.bestFit();
        bestSol.updateAtributes();
        D = new double[bpp.N][(int) (bestSol.binCount * 1.5)];
        for (int i = 0; i < D.length; i++) {
            fill(D[i], 1.0 / D[0].length);
        }

        P = new double[bpp.N][(int) (bestSol.binCount * 1.5)];


        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
    }

    private Sol samplingD() {
        Utils.shuffler(idx);
        Sol sol = new Sol(bpp);
        double p[] = new double[D[0].length];
        int load[] = new int[D[0].length];
        for (int i : idx) {
            for (int j = 0; j < p.length; j++)
                p[j] = (bpp.size[i] + load[j] <= bpp.C) ? D[i][j] : 0;
            int x = Utils.roleta(p);
            if (x < 0)
                return null;
            load[x] += bpp.size[i];
            sol.binOf[i] = x;
        }
        for (int j = 0; j < load.length; j++)
            if (load[j] == 0) {
                //elemina pacote vazio
                for (int k = 0; k < sol.binOf.length; k++)
                    if (sol.binOf[k] > j)
                        sol.binOf[k]--;
            }
        sol.updateAtributes();
        return sol;
    }

    @Override
    public int run() {

        ArrayList<Sol> list = new ArrayList<>();
        for (int k = 0; k < ite; k++) {
            list.clear();
            for (int i = 0; i < sampleSize; i++) {
                Sol s = samplingD();
                if (s != null && !list.contains(s))
                    list.add(s);
            }
            Collections.sort(list);
            if (bestSol.binCount > list.get(0).binCount) {
                bestSol = list.get(0);
                System.out.println("DEA " + bestSol);
            }

            calculaP(list.subList(0, subSampleSize));

            for (int i = 0; i < D.length; i++)
                for (int j = 0; j < D[0].length; j++)
                    D[i][j] = D[i][j] * (1 - alpha) + P[i][j] * alpha;

            System.out.println(list.get(0));
        }


        return bestSol.binCount;
    }

    private void calculaP(List<Sol> list) {
        for (int i = 0; i < P.length; i++)
            fill(P[i], 0);
        double x = (double) 1 / list.size();
        for (Sol s : list)
            for (int i = 0; i < bpp.N; i++)
                P[i][s.binOf[i]] += x;


    }

    @Override
    public Sol getSol() {
        return bestSol;
    }
}
