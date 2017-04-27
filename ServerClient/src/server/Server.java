package server;

/*
 * Created by douglas.leite on 27/04/2017.
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;

class Server {
    static Semaphore s;

    public static void main(String argv[]) throws Exception {
        s = new Semaphore(2);

        // Cria socket do servidor
        ServerSocket welcomeSocket = new ServerSocket(4242);

        while (true) {
            if (s.tryAcquire()) {
                //Permite ao servidor processar 2 requests por vez.
                Socket connectionSocket = welcomeSocket.accept();

                FileTransfer fileTransfer = new FileTransfer(connectionSocket, s);

                Thread thread = new Thread(fileTransfer);

                thread.start();
            } else {
                welcomeSocket.close();
                System.out.println("limite de conexões");
                synchronized (Server.s) {
                    s.wait();
                }
                welcomeSocket = new ServerSocket(4242);
            }
        }
    }
}

class FileTransfer implements Runnable {
    File file;

    int packetSize;
    String clientMessage;
    String serverMessage;

    Socket socket;
    Semaphore semaphore;
    BufferedReader inFromClient;
    DataOutputStream outToClient;

    public FileTransfer(Socket connectionSocket, Semaphore semaphore) {
        this.socket = connectionSocket;
        this.semaphore = semaphore;
        packetSize = 1024;
    }

    @Override
    public void run() {
        try {
            // Stream de entrada para dados do cliente
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Stream de saída para dados para o cliente
            outToClient = new DataOutputStream(socket.getOutputStream());

            // Espera mensagem do cliente
            clientMessage = inFromClient.readLine();

            // Busca o arquivo informado
            file = new File("ServerClient/Files/Server/" + clientMessage);

            System.out.println(file.getAbsolutePath());

            if (file.exists()) {
                // Se o arquivo existir, envia o tamanho dele para o cliente
                serverMessage = Long.toString(file.length()) + "\n";
                outToClient.writeBytes(serverMessage);

                byte[] bytes = new byte[packetSize];
                InputStream fileStream = new FileInputStream(file);

                int count;

                while ((count = fileStream.read(bytes)) > 0) {
                    outToClient.write(bytes, 0, count);
                }

                fileStream.close();

            } else {
                // Se o arquivo não existir, avisa o cliente
                serverMessage = "Arquivo inexistente!\n";
                outToClient.writeBytes(serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                inFromClient.close();
                outToClient.close();
                semaphore.release();
                synchronized (Server.s) {
                    Server.s.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}