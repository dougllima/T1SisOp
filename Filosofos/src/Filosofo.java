import java.util.concurrent.Semaphore;

/**
 * Created by douglas.leite on 19/04/2017.
 */
public class Filosofo implements Runnable{
    public int index;
    public Semaphore lFork;
    public Semaphore rFork;

    public Filosofo(int index, Semaphore lFork, Semaphore rFork) {
        this.index = index;
        this.lFork = lFork;
        this.rFork = rFork;
    }

    @Override
    public void run() {
        while(true){
            if(lFork.tryAcquire(1)){
                if(rFork.tryAcquire(1)){
                    // Come 2
                    Logger.Comer(index);
                }else{
                    // Espera 0~3
                    Logger.Tentar(index);
                }
            }else{
                // Pensa 5
                Logger.Pensar(index);
            }
        }
    }
}
