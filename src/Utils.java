import java.util.Random;

public class Utils {
    public static Random rd = new Random(20000);
    public static void shuffler(int v[]){
        for(int i = v.length-1; i > 1; i--){
            int x = rd.nextInt(i);
            int aux = v[i];
            v[i] = v[x];
            v[x] = aux;
        }
    }
}
