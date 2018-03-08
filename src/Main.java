import java.io.FileNotFoundException;

public class Main {
    public static void main(String args[]) throws FileNotFoundException {
        BPP bpp = new BPP("instances/Hard28_BPP900.txt");
        System.out.println(bpp);
        System.out.println("LB: "+bpp.LB());
        Sol sol = new Sol(bpp);

        int s = sol.worstFit();
        System.out.println(sol);

        HillClimbing hc = new HillClimbing(bpp,sol);
        hc.run();

        System.out.println(sol);



    }
}
