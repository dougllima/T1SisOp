import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Created by douglas.leite on 19/04/2017.
 */
public class Mesa {
    public Filosofo[] filosofos;
    public Semaphore[] forks;
    public int time, qnt;

    public Mesa(int time, int qnt) {
        this.time = time;
        this.qnt = qnt;

        forks = new Semaphore[qnt];
        filosofos = new Filosofo[qnt];

        for (int i = 0; i < qnt; i++) {
            forks[i] = new Semaphore(1);
        }
        for (int i = 0; i < qnt; i++) {
            int left = i == 0 ? qnt - 1 : i - 1,
                    right = i == (qnt - 1) ? 0 : i + 1;
            //inicial cada filosofo, associando o garfo de antes e depois
            filosofos[i] = new Filosofo(i, qnt, forks[left], forks[right]);
        }
    }

    public void run() {
        //define o fim do programa

        for (Filosofo f : filosofos) {
            Thread t = new Thread(f);
            f.setThread(t);
            f.setEndTime(System.currentTimeMillis() + time * 1000);
            t.start();
        }
    }
}
