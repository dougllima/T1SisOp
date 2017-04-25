import java.util.concurrent.Semaphore;

/**
 * Created by douglas.leite on 19/04/2017.
 */
public class Filosofo implements Runnable {
    public int index, cont, qnt;
    public long endTime;
    Semaphore lFork;
    Semaphore rFork;
    Thread thread;

    public Filosofo(int index, int qnt, Semaphore lFork, Semaphore rFork) {
        this.index = index;
        this.lFork = lFork;
        this.rFork = rFork;
        this.qnt = qnt;
        cont = 0;
    }

    @Override
    public void run() {
        //Pega o tempo previsto pro fim do programa, e tira 5 segundos,
        // assim garante que não passa do tempo estipulado.
        while (System.currentTimeMillis() <= (endTime - 5000)) {
            //se não comeu duas vezes seguidas, tenta pegar o garfo da esquerda
            if (cont < 5 && lFork.tryAcquire(1)) {
                //se pegou o garfo da esquerda, tenta pegar o garfo da direita
                if (rFork.tryAcquire(1)) {
                    //se pegou os dois, come por 2 segundos e solta os garfos
                    Comer();
                } else {
                    //se não, solta o garfo da esquerda, espera e tenta de novo
                    Esperar();
                }
            } else {
                //se não, pensa
                Pensar();
            }
        }
        Logger.Fim();
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void Comer() {
        try {
            lFork.release();
            rFork.release();
            cont++;
            Logger.Comer(index);
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Esperar() {
        try {
            // Espera 0~3
            cont = 0;
            lFork.release();
            Logger.Tentar(index);
            thread.sleep(1000 * ((int) (Math.random() * 10000) % 4));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Pensar() {
        try {
            cont = 0;
            // Pensa 5
            Logger.Pensar(index);
            thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
