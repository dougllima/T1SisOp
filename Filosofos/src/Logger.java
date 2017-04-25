/**
 * Created by douglas.leite on 19/04/2017.
 */
public class Logger {
    public int qntF;
    public static FilosofoLog[] log;
    public static boolean acabou = false;

    class FilosofoLog {
        int index;
        int comeu = 0;
        int tentou = 0;
        int pensou = 0;

        public FilosofoLog(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return String.format("Filosofo %d: Comeu %d vezes -> Tentou %d vezes -> Pensou %d vezes", index, comeu, tentou, pensou);
        }
    }

    public Logger(int qntF) {
        this.qntF = qntF;
        log = new FilosofoLog[qntF];
        for (int i = 0; i < qntF; i++) {
            log[i] = new FilosofoLog(i);
        }
    }

    public static void Fim(){
        if(!acabou) {
            acabou = true;
            for (FilosofoLog l : log) {
                System.out.println(l.toString());
            }
        }
    }

    public static void Comer(int i) {
        log[i].comeu++;
    }

    public static void Pensar(int i) {
        log[i].pensou++;
    }

    public static void Tentar(int i) {
        log[i].tentou++;
    }
}
