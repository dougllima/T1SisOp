import java.util.concurrent.Semaphore;

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
        for (int i = 0; i < qnt; i++) {
            forks[i] = new Semaphore(1);
        }
        for (int i = 0; i < qnt; i++) {
            int left = i == 0 ? qnt - 1 : i - 1,
                right = i == (qnt - 1) ? 0 : i + 1;
            filosofos[i] = new Filosofo(i, forks[left], forks[right]);
        }
    }
}
