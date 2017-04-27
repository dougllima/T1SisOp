package client;

/**
 * Created by douglas.leite on 27/04/2017.
 */

import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception {
        int packetSize = 1024;
        long fileSize;
        String clientMessage;
        String serverMessage;

        // Permite leitura de dados do teclado
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Cria o socket cliente indicando o IP e porta de destino. 
        try {
            Socket clientSocket = new Socket("127.0.0.1", 4242);

            // Cria uma stream de saída para enviar dados para o servidor
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Cria uma stream de entrada para receber os dados do servidor
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Lê o nome do arquivo desejado
            System.out.print("Informe o arquivo desejado: ");
            clientMessage = inFromUser.readLine();

            // Envia para o servidor. Não esquecer do \n no final para permitir que o servidor use readLine
            outToServer.writeBytes(clientMessage + '\n');

            // Lê mensagem de resposta do servidor
            serverMessage = inFromServer.readLine();
            if (!serverMessage.isEmpty()) {
                if (serverMessage.substring(0, 1).matches("[0-9]")) {
                    fileSize = Long.valueOf(serverMessage);

                    if (fileSize > Integer.MAX_VALUE) {
                        System.out.println("Arquivo muito grande para ser transferido");
                        clientSocket.close();
                    } else {
                        System.out.println("Iniciando transferencia");

                        BufferedInputStream inputStream;
                        FileOutputStream outputStream;
                        try {
                            File file = new File(String.format("ServerClient/Files/Client/%s", clientMessage));
                            int files = 0;
                            while (file.exists()) {
                                file = new File(String.format("ServerClient/Files/Client/(%d)%s", files, clientMessage));
                                files++;
                            }

                            inputStream = new BufferedInputStream(clientSocket.getInputStream());
                            outputStream = new FileOutputStream(file);

                            int read;
                            byte[] bytes = new byte[(int) fileSize];

                            while ((read = inputStream.read(bytes, 0, bytes.length)) != -1) {
                                outputStream.write(bytes, 0, read);
                            }

                        } finally {
                            System.out.println("Transferencia finalizada!");

                            // Encerra conexão
                            clientSocket.close();
                        }
                    }
                } else {
                    clientSocket.close();
                    System.out.println(serverMessage);
                }
            }
        } catch (ConnectException ex) {
            System.out.println("Conexão recusada");
        }
    }
}