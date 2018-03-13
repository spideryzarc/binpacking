import java.io.FileNotFoundException;

public class Main {
    public static void main(String args[]) throws FileNotFoundException {
//        BPP bpp = new BPP("instances/Hard28_BPP900.txt");
        BPP bpp = new BPP("instances/N4W2B1R4.txt");
//        BPP bpp = new BPP("instances/Hard28_BPP13.txt");
        System.out.println(bpp);
        System.out.println("LB: "+bpp.LB());
        Sol sol = new Sol(bpp);

        int s = sol.worstFit();
        System.out.println(sol);
//
//        HillClimbing hc = new HillClimbing(bpp,sol);
//        hc.run();
//        RMS rms = new RMS(bpp,sol);
//        rms.run(1000);

//        ILS ils = new ILS(bpp,sol);
//        ils.run(1000,30);

        VND vnd = new VND(bpp,sol);
        vnd.run();
        System.out.println(sol);



    }
}
