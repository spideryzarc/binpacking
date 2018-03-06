import java.io.FileNotFoundException;

public class Main {
    public static void main(String args[]) throws FileNotFoundException {
        BPP bpp = new BPP("instances/Hard28_BPP900.txt");
        System.out.println(bpp);
        System.out.println("LB: "+bpp.LB());
        Sol sol = new Sol(bpp);
        int s = sol.bestFit();
        System.out.println(sol);

        s = sol.worstFit();
        System.out.println(sol);

        //for (int i = 0; i < s; i++) {
        //    System.out.println(sol.load(i));
        //}

    }
}
