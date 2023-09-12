import java.util.*;

public class P {
    public static void p(String input) {
        System.out.print(input);
    }

    public static void pl(String input) {
        System.out.println(input);
    }

    public static void pl() {
        System.out.println();
    }

    public static void s() {
        System.out.println("\n**************************************************\n");
    }

    public static <T> void list(Collection<T> list) {
        for (T o : list) {
            String s = "null";
            if (o != null) {
                s = o.toString();
            }
            System.out.print(" " + s);
        }
        System.out.println();
    }
}
