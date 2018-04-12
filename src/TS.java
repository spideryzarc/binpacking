import java.util.LinkedList;

import static java.util.Arrays.fill;

/**
 * Tabu Search
 */
public class TS implements Solver {
    Sol bestSol;
    BPP bpp;
    int ite, idx[], tenure;
    LinkedList<Sol> list = new LinkedList<>();


    /**
     * @param ite    numero de ótimos locais até parar
     * @param tenure quantidade de ótimos locais armazenados na memória tabu
     */
    public TS(int ite, int tenure) {
        this.ite = ite;
        this.tenure = tenure;
    }

    @Override
    public void setBPP(BPP bpp) {
        this.bpp = bpp;
        bestSol = new Sol(bpp);
        idx = new int[bpp.N];
        for (int i = 0; i < idx.length; i++)
            idx[i] = i;
        list.clear();
    }

    int load[];

    @Override
    public int run() {
        //constroi solução inicial
        Sol sol = new Sol(bpp);
        Utils.shuffler(idx);
        int bestCount = sol.bestFitRandom(idx);
        bestSol.copy(sol);
        load = new int[bestCount];
        for (int i = 0; i < sol.binOf.length; i++)
            load[sol.binOf[i]] += bpp.size[i];

        for (int i = 0; i < ite; i++) {
            int x = hc(sol);
//            double avg = (double) bpp.sizeSum / x;
//            double a = stdDev(load, avg);
//            System.out.println("a " + a);
            if (x < bestCount) {
                bestCount = x;
                bestSol.copy(sol);
                System.out.println("TS: " + bestSol.binCount());
                list.clear();
            }

            addTabu(sol);
            while (!pertub(sol))
                ;
        }

        return bestSol.binCount();
    }

    private boolean pertub(Sol sol) {
        int count = load.length;

        if (Utils.rd.nextBoolean()) { //um item troca de pacote
            int idx[] = new int[count];
            for (int i = 0; i < count; i++)
                idx[i] = i;
            Utils.shuffler(idx);

            for (int pack : idx) {
                //int pack = Utils.rd.nextInt(count);
                int sobra = bpp.C - load[pack];
                int x;
                for (x = bpp.N - 1; x >= 0 && bpp.size[x] <= sobra; x--) {
                    //nada
                }
                int s = bpp.N - x - 1;
                if (s > 0) {
                    int item = Utils.rd.nextInt(s) + x + 1;
                    if (sol.binOf[item] != pack) {
                        int bi = sol.binOf[item];
                        sol.binOf[item] = pack;
                        load[bi] -= bpp.size[item];
                        load[pack] += bpp.size[item];

                        //se o pacote do item esvaziar
                        if (load[bi] == 0) {
                            //elemina pacote vazio
                            for (int k = 0; k < bpp.N; k++)
                                if (sol.binOf[k] > bi)
                                    sol.binOf[k]--;
                            count--;
                            load = new int[count];
                            for (int i = 0; i < bpp.N; i++)
                                load[sol.binOf[i]] += bpp.size[i];

                        }
                        if (ehTabu(sol)) {
                            sol.binOf[item] = bi;
                            load[bi] += bpp.size[item];
                            load[pack] -= bpp.size[item];
                        } else
                            return true;
                    }
                }
            }

        }

        // dois itens trocam de pacotes entre si
        int idx[] = new int[bpp.N];
        for (int i = 0; i < bpp.N; i++)
            idx[i] = i;
        Utils.shuffler(idx);
        for (int a = 1; a < bpp.N; a++) {
            int i = idx[a];
            int bi = sol.binOf[i];
            for (int b = 0; b < a; b++) {
                int j = idx[b];
                int bj = sol.binOf[j];

                if (bi != bj
                        && bpp.size[i] != bpp.size[j]
                        && load[bi] - bpp.size[i] + bpp.size[j] <= bpp.C
                        && load[bj] - bpp.size[j] + bpp.size[i] <= bpp.C) {
                    sol.binOf[i] = bj;
                    sol.binOf[j] = bi;
                    load[bi] += -bpp.size[i] + bpp.size[j];
                    load[bj] += -bpp.size[j] + bpp.size[i];
                    if (ehTabu(sol)) {
                        sol.binOf[i] = bi;
                        sol.binOf[j] = bj;
                        load[bi] -= -bpp.size[i] + bpp.size[j];
                        load[bj] -= -bpp.size[j] + bpp.size[i];
                    } else
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * Elimina k pacotes aleatórios e reempacotas seus itens em ordem aleatória
     *
     * @param k      numero de pacotes eliminados
     * @param output saída a ser pertubada
     */
    private void pertub2(int k, Sol output) {
        int n = output.binCount();
        for (int i = 0; i < k; i++) {
            int x = Utils.rd.nextInt(n);
            for (int j = 0; j < bpp.N; j++)
                if (output.binOf[j] == x)
                    output.binOf[j] = -1;
                else if (output.binOf[j] > x)
                    output.binOf[j]--;
        }
        int count = fit(output);
        if (count != load.length)
            load = new int[count];
        else
            fill(load, 0);
        for (int i = 0; i < bpp.N; i++)
            load[output.binOf[i]] += bpp.size[i];

    }

    /**
     * empacota os itens sem pacote (binof == -1) em ordem randomica
     */
    private int fit(Sol sol) {
        int binOf[] = sol.binOf;
        int binCount = sol.binCount();
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

    private void addTabu(Sol sol) {
        Sol aux = null;

        if (list.size() < tenure)
            aux = new Sol(bpp);
        else
            aux = list.removeLast();

        aux.copy(sol);
        list.addFirst(aux);
    }


    @Override
    public Sol getSol() {
        return bestSol;
    }

    public double stdDev(int[] load, double avg) { // desvio
        double soma = 0;
        for (int i = 0; i < load.length; i++) {
            double x = (load[i] - avg);
            soma += x * x;
        }
        return soma;
    }

    /**
     * Hill climbing
     */
    public int hc(Sol sol) {
        int count = sol.binCount();
        int binof[] = sol.binOf;
        int size[] = bpp.size;

        double avg = (double) bpp.sizeSum / count;
        double a = stdDev(load, avg);
//        System.out.println("a "+a);

        boolean moved;
        //hillclimb
        do {
            moved = false;
            //busca local
            ls:
            for (int i = 0; i < binof.length; i++) {//para todo_ item i
                int bi = binof[i];
                for (int j = 0; j < count; j++)//para todo_ pacote j
                    if (bi != j && load[j] + size[i] <= bpp.C) {//se ele i cabe em j
                        load[j] += size[i];
                        load[bi] -= size[i];
                        binof[i] = j;
                        double x = stdDev(load, avg);
                        if (x > a + 0.001 && !ehTabu(sol)) {// se mover i para j melhora a solução
                            a = x;
                            if (load[bi] == 0) { //se  pacote bi ficou vazio
                                //elemina pacote vazio
                                for (int k = 0; k < binof.length; k++)
                                    if (binof[k] > bi)
                                        binof[k]--;
                                count--;

                                //atualiza vetor load
                                load = new int[count];
                                for (int k = 0; k < binof.length; k++)
                                    load[binof[k]] += size[k];

                                //atualiza média e avaliação corrente
                                avg = (double) bpp.sizeSum / count;
                                a = stdDev(load, avg);

                            }
                            moved = true;
                            break ls;
                        } else {
                            load[j] -= size[i];
                            load[bi] += size[i];
                            binof[i] = bi;
                        }
                    }
            }
            if (!moved) {
                ls:
                for (int i = 0; i < binof.length; i++) {//para todo_ item i
                    int bi = binof[i];
                    for (int j = 0; j < i; j++) {
                        int bj = binof[j];
                        if (bi != bj
                                && load[bi] - bpp.size[i] + bpp.size[j] <= bpp.C
                                && load[bj] - bpp.size[j] + bpp.size[i] <= bpp.C) {
                            binof[i] = bj;
                            binof[j] = bi;
                            load[bi] += -bpp.size[i] + bpp.size[j];
                            load[bj] += -bpp.size[j] + bpp.size[i];
                            double x = stdDev(load, avg);
                            if (x > a + 0.001 && !ehTabu(sol)) {// se mover i para j melhora a solução
                                a = x;
                                moved = true;
                                break ls;
                            } else {
                                binof[i] = bi;
                                binof[j] = bj;
                                load[bi] -= -bpp.size[i] + bpp.size[j];
                                load[bj] -= -bpp.size[j] + bpp.size[i];
                            }
                        }
                    }
                }


            }
        } while (moved);


        return count;
    }

    private boolean ehTabu(Sol sol) {
        boolean t = list.contains(sol);
//        if(t)
//            System.err.println("tabu");
        return t;
    }

}
