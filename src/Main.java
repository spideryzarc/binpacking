import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static double benchmark(String dir, Solver solver) throws FileNotFoundException {
        File file = new File(dir);
        File arq[] = file.listFiles((d, name) -> name.endsWith(".txt"));
        Arrays.sort(arq);
        int count = 0;
        double gap = 0;
        long time = 0;
//        System.out.println(solver);
        for (File f : arq) {
            count++;
            BPP bpp = new BPP(f.getPath());
            Sol sol;
            double lb = bpp.LB();

            Utils.rd.setSeed(7);

            solver.setBPP(bpp);
            long t = System.currentTimeMillis();
            int x = solver.run();
            t = System.currentTimeMillis() - t;
            time += t;
            gap += x / lb - 1;
            System.out.println(count + " - " + x + "  " + f + ": " + (x / lb - 1) + " T: " + t);
        }
        System.out.printf("%s\t %.2f\t %d\n", solver, 100 * gap / count, time / count);

        return 100 * gap / count;
    }

    public static void main(String args[]) throws FileNotFoundException {
//        String dir = "instances/Scholl/Scholl_1";
        String dir = "instances/Hard28";
//        String dir = "instances/Wascher";
//        benchmark(dir, new RMS(10));

//        benchmark(dir, new VNS(10,10,5));
//        benchmark(dir, new ILS(10, 100));
//        benchmark(dir, new GRASP(50, 10));
//          benchmark(dir, new GLS(100));
        benchmark(dir, new SA(1000000,1,.9999));

//        BPP bpp = new BPP("instances/N4W2B1R4.txt");
//        Sol sol = new Sol(bpp);
//        sol.worstFit();
//        VND vnd = new VND(bpp,sol);
//        vnd.run();


    }
}
