public interface Solver {
    void setBPP(BPP bpp);

    int run();

    Sol getSol();
}
