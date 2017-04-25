import java.util.Scanner;

/**
 * Created by douglas.leite on 19/04/2017.
 */
public class Main {
    public static void main(String args[]) {
        int qnt = 10,
            time = 60;

        Mesa m = new Mesa(time,qnt);

        Logger l = new Logger(qnt);
        m.run();
    }
}
