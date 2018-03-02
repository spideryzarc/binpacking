public class Main {
    public static void main(String args[]) {
        BPP bpp = new BPP();
        bpp.C = 9;
        bpp.size = new int[]{6, 6, 5, 5, 5, 4, 4, 4, 4, 2,
                2, 2, 2, 3, 3, 7, 7, 5, 5, 8, 8, 4, 4, 5};
        bpp.N = bpp.size.length;

        System.out.println(bpp);

        Sol sol = new Sol(bpp);
        sol.binOf = new int[]{4,5,6,7,8,6,7,8,9,2,3,12,12,
                4,5,2,3,10,11,0,1,10,11,9};
        System.out.println(sol);
        System.out.println(sol.binCount());
        System.out.println(sol.load(1));


    }
}
